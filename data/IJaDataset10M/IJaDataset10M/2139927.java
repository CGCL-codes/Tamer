package edu.ucdavis.genomics.metabolomics.binbase.algorythm.export;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.export.dest.QuantificationTableDestinationFactoryImpl;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.methods.GitterMethode;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.Experiment;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentClass;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentSample;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.Result;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.ResultLink;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.Sample;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.util.hibernate.HibernateFactory;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsService;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsServiceFactory;
import edu.ucdavis.genomics.metabolomics.util.SQLObject;
import edu.ucdavis.genomics.metabolomics.util.config.Configable;
import edu.ucdavis.genomics.metabolomics.util.io.dest.Destination;
import edu.ucdavis.genomics.metabolomics.util.io.dest.DestinationFactory;

/**
 * really simple and slow service, should be replace with a faster version as
 * soon as possible
 * 
 * @author wohlgemuth
 * @version Nov 18, 2005
 * 
 */
public class SimpleExportService extends SQLObject implements ExportService {

    private Properties p = Configable.CONFIG.getConfigProvider().getProperties();

    public void setProperties(Properties p) {
        this.p = p;
    }

    public SimpleExportService() {
        super();
    }

    /**
	 * export the experiment
	 * 
	 * @author wohlgemuth
	 * @version Nov 18, 2005
	 * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.export.ExportService#export(edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.Experiment)
	 */
    public int export(Experiment ex) {
        Collection ids = new Vector();
        Session hsession = HibernateFactory.newInstance(p).getSession();
        ExperimentClass cl[] = ex.getClasses();
        Transaction t = hsession.beginTransaction();
        Result result = new Result();
        result.setSetupXId(ex.getId());
        result.setDescription("no description available");
        hsession.save(result);
        int resultId = result.getId();
        for (int i = 0; i < cl.length; i++) {
            ExperimentSample sample[] = cl[i].getSamples();
            for (int x = 0; x < sample.length; x++) {
                Criteria crit = hsession.createCriteria(Sample.class);
                crit.add(Expression.eq("setupxId", sample[x].getId()));
                crit.add(Expression.eq("visibleString", "TRUE"));
                crit.addOrder(Order.asc("version"));
                Collection sampleCollection = crit.list();
                if (sampleCollection.isEmpty()) {
                    logger.warn("no sample found: " + sample[x].getId());
                }
                Iterator it = sampleCollection.iterator();
                while (it.hasNext()) {
                    Sample sa = (Sample) it.next();
                    ids.add(sa.getId());
                    ResultLink link = new ResultLink();
                    link.setSample(sa);
                    link.setResult(result);
                    sa.getResultLinks().add(link);
                    result.addLink(link);
                    hsession.save(link);
                    hsession.refresh(sa);
                }
            }
        }
        t.commit();
        hsession.close();
        try {
            GitterMethode method = new GitterMethode();
            method.setNewBinAllowed(false);
            method.setConnection(this.getConnection());
            Iterator it = ids.iterator();
            while (it.hasNext()) {
                Integer in = (Integer) it.next();
                method.setSampleId(in.intValue());
                method.run();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Map results = new HashMap();
        try {
            SampleExport export = new SampleExport();
            export.setConnection(this.getConnection());
            Iterator it = ids.iterator();
            while (it.hasNext()) {
                Integer in = (Integer) it.next();
                results.put(in, export.getQuantifiedSampleAsXml(in.intValue()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Map map = new HashMap();
        map.put("CONNECTION", this.getConnection());
        try {
            Iterator it = ids.iterator();
            while (it.hasNext()) {
                Integer id = (Integer) it.next();
                String res = (String) results.get(id);
                Destination des = DestinationFactory.newInstance(QuantificationTableDestinationFactoryImpl.class.getName()).createDestination(id, map);
                OutputStream out = des.getOutputStream();
                InputStream in = new ByteArrayInputStream(res.getBytes());
                int size = 1024 * 16;
                byte[] buffer = new byte[size];
                int length;
                while ((length = in.read(buffer, 0, size)) != -1) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultId;
    }
}
