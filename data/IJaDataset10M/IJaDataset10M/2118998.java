package de.fzi.mappso.particlecluster.aws;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import de.fzi.kadmos.api.IncompatibleOntologyException;
import de.fzi.kadmos.api.impl.OWLOntologyDictionary;
import de.fzi.kadmos.cloud.CloudController;
import de.fzi.kadmos.cloud.CloudException;
import de.fzi.kadmos.cloud.aws.AWSController;
import de.fzi.mappso.align.AlignmentParticle;
import de.fzi.mappso.align.Config;
import de.fzi.mappso.align.MapPSOAlignment;
import de.fzi.mappso.align.MapPSOAlignmentFactory;
import de.fzi.mappso.align.MapPSOConfigurationException;

/**
 * This Class implements the computing part of a AWSCluster on a AMI.
 * @author Carsten Daenschel
 *
 */
public class WorkerCluster implements Runnable {

    private static final Log logger = LogFactory.getLog(WorkerCluster.class);

    private final int port;

    private Config param;

    private final CloudController com;

    private List<AlignmentParticle> particles;

    private MapPSOAlignment localBest, globalBest;

    /**
	 * Using an {@link AWSController} and the given port.
	 */
    public WorkerCluster(int port) {
        particles = new ArrayList<AlignmentParticle>();
        this.port = port;
        com = new AWSController();
    }

    /**
	 * Using given {@link Cloudcontroller} and port.
	 */
    public WorkerCluster(int socket, CloudController cloud) {
        particles = new ArrayList<AlignmentParticle>();
        this.port = socket;
        com = cloud;
    }

    @Override
    public void run() {
        try {
            initConnection();
            logger.info("connected to server");
            initParticles();
            logger.info("init done. using " + particles.size() + " particles");
            compute();
            closeConnection();
        } catch (CloudException e) {
            final String errMsg = "Cloud related exception.";
            logger.fatal(errMsg, e);
        } catch (MapPSOConfigurationException e) {
            final String errMsg = "Problem with configuration parameters when initialising or updating particle.";
            logger.error(errMsg, e);
        } catch (IncompatibleOntologyException e) {
            final String errMsg = "Internal error: Incompatible ontology when reporting new global best to particles.";
            logger.fatal(errMsg, e);
        }
    }

    private void initConnection() throws CloudException {
        Properties controllerProp = new Properties();
        controllerProp.setProperty("awsPort", Integer.toString(port));
        com.setParameter(controllerProp);
        com.connect();
    }

    /**
	 * Receives the first two messages from the controller which contain a {@link Config} and the size of the local cluster.
	 * It then parses the ontologies and initializes <code>size</code> {@link AlignmentParticle}.
	 * @throws CloudException
	 */
    private void initParticles() throws CloudException {
        OWLOntology onto1, onto2;
        int size;
        Object msg = com.receive();
        if (!(msg instanceof Config)) {
            throw new CloudException("First recieved Object was not a Config. It was " + msg.getClass() + ".");
        }
        param = (Config) msg;
        param.setAsMainConfig();
        logger.debug("Reading Config successful.");
        msg = com.receive();
        if (!(msg instanceof Integer)) {
            throw new CloudException("First recieved Object was not an Integer. It was " + msg.getClass() + ".");
        }
        size = (Integer) msg;
        logger.debug("Building " + size + " AlignmentParticles.");
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            logger.info("Loading first Ontology: " + param.getOntology1URL());
            IRI ontoIRI = IRI.create(param.getOntology1URL());
            onto1 = manager.loadOntology(ontoIRI);
            OWLOntologyDictionary.getInstance().put(onto1);
            logger.info("Loading second Ontology: " + param.getOntology2URL());
            ontoIRI = IRI.create(param.getOntology2URL());
            onto2 = manager.loadOntology(ontoIRI);
            OWLOntologyDictionary.getInstance().put(onto2);
        } catch (OWLOntologyCreationException e) {
            com.send(e);
            throw new CloudException("Error while reading Ontology.", e);
        } catch (MapPSOConfigurationException e) {
            com.send(e);
            throw new CloudException("Error while reading Ontology.", e);
        } catch (URISyntaxException e) {
            com.send(e);
            throw new CloudException("Error while reading Ontology.", e);
        }
        localBest = MapPSOAlignmentFactory.getInstance().createAlignment(onto1, onto2);
        globalBest = MapPSOAlignmentFactory.getInstance().createAlignment(onto1, onto2);
        try {
            for (int i = 0; i < size; i++) {
                particles.add(new AlignmentParticle(i, onto1, onto2, localBest, null));
            }
        } catch (Exception e) {
            com.send(e);
            throw new CloudException("Error while initialising particle.", e);
        }
    }

    private void compute() throws CloudException, MapPSOConfigurationException, IncompatibleOntologyException {
        int iteration = param.getIterations();
        logger.info("Starting to compute " + iteration + " steps.");
        while (iteration-- > 0) {
            logger.info("Starting iteration " + (particles.iterator().next().getIteration() + 1) + ".");
            updateParticles();
            logger.info("Done. Starting communication.");
            sendBest();
            Object msg = com.receive();
            if (msg == null || msg instanceof MapPSOAlignment) {
                processResponse((MapPSOAlignment) msg);
                logger.info("Processed Response.");
            }
        }
    }

    /**
	 * This method creates for each {@link AlignmentParticle} a Thread and runs one iteration.
	 */
    private void updateParticles() {
        List<Thread> workerthreads = new ArrayList<Thread>();
        for (final AlignmentParticle a : particles) {
            Thread help = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        a.update();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            help.start();
            workerthreads.add(help);
            logger.trace("Started a Thread.");
        }
        for (Thread a : workerthreads) {
            boolean asleep = true;
            while (asleep) {
                try {
                    a.join();
                    asleep = false;
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
	 * Searches the local best alignment and sends it.
	 * If no better alignment was found null is send.
	 * @throws CloudException
	 */
    private void sendBest() throws CloudException {
        boolean newBest = false;
        for (AlignmentParticle a : particles) {
            if (a.getPersonalBest().getFitness() < localBest.getFitness()) {
                localBest = a.getPersonalBest();
                newBest = true;
            }
        }
        if (newBest) {
            logger.debug("Sending local Best to controller.");
            com.send(localBest);
        } else {
            logger.debug("Sending null to controller.");
            com.send(null);
        }
    }

    /**
	 * Sends the best alignment of the old global best, the local best and the new received global best to all particles.
	 * @param msg the MapPSOAlignment or null
	 * @throws IncompatibleOntologyException 
	 */
    private void processResponse(MapPSOAlignment msg) throws IncompatibleOntologyException {
        if (localBest.getFitness() > globalBest.getFitness()) globalBest = localBest;
        if (msg != null && msg.getFitness() > globalBest.getFitness()) globalBest = msg;
        for (AlignmentParticle a : particles) if (a.getGlobalBest().getFitness() < localBest.getFitness()) a.setGlobalBest(localBest);
    }

    private void closeConnection() throws CloudException {
        com.send("Computation Done");
        com.close();
    }
}
