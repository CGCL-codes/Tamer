package de.l3s.sr.core;

import de.l3s.sr.core.SRPropManager;
import de.l3s.sr.mapper.UriMapper;
import de.l3s.sr.mapper.DefaultUriMapper;
import de.l3s.sr.microreader.JenaRdfaReader;
import de.l3s.sr.microreader.RdfaReader;
import de.l3s.sr.rdf.ImplementationFactory;
import de.l3s.sr.rdf.InstanceFactory;
import de.l3s.sr.rdf.JavassistImplFactory;
import de.l3s.sr.rdf.SommerInstanceFactory;

/**
 * created on 29.07.2008 This class/interface
 * 
 * @author Eugen Okon, <a href="mailto:eugenokon@gmx.de">eugenokon@gmx.de</a>
 * @author SemREST-Team, <a href="mailto:semrest@kbs.uni-hannover.de">semrest@kbs.uni-hannover.de</a>
 * @author last edited by: $Author: team $
 * @version $Revision: 1.1 $ $Date: 2009-04-06 17:51:52 $
 */
public class SREngine {

    /**
	 * @uml.property name="uriMapper"
	 * @uml.associationEnd
	 */
    private UriMapper uriMapper;

    private ImplementationFactory implementationFactory;

    /**
	 * this is the sufix used by ImplementationFactory to create an implemented
	 * .class File e.g. for the sufix "Impl" and interface to implement
	 * "MyInterface.class" the Result ist MyInterface_Impl.class
	 */
    public static String IMLP_NAME_SUFFIX = "Impl";

    private InstanceFactory instanceFactory;

    private RdfaReader rdfaReader;

    private static SREngine instance = null;

    /**
	 * tries to read the UriMapper to use from SemRestProperties in case none
	 * is declared the UriDefaultMapper is used
	 */
    @SuppressWarnings("unchecked")
    private SREngine() {
        uriMapper = null;
        boolean useDefault = false;
        String mapperToUse = SRPropManager.getString(SRPropManager.URI_MAPPER_TO_USE);
        if (null != mapperToUse) {
            try {
                Class mapperToUseClass = Class.forName(mapperToUse);
                uriMapper = (UriMapper) mapperToUseClass.newInstance();
            } catch (ClassNotFoundException e) {
                useDefault = true;
                System.out.println("SREngine: could not find the Class " + mapperToUse + ". Using UriDefaultMapper.");
            } catch (InstantiationException e) {
                useDefault = true;
                System.out.println("SREngine: could not instance the Class " + mapperToUse + ". Using UriDefaultMapper.");
            } catch (IllegalAccessException e) {
                useDefault = true;
                e.printStackTrace();
            }
        } else {
            useDefault = true;
        }
        if (useDefault) {
            uriMapper = new DefaultUriMapper();
        }
        useDefault = false;
        String implementationFactoryToUse = SRPropManager.getString(SRPropManager.IMPLEMENTATION_FACTORY_TO_USE);
        if (null != implementationFactoryToUse) {
            try {
                Class implementationFactoryToUseClass = Class.forName(implementationFactoryToUse);
                implementationFactory = (ImplementationFactory) implementationFactoryToUseClass.newInstance();
            } catch (ClassNotFoundException e) {
                useDefault = true;
                System.out.println("SREngine: could not find the Class " + implementationFactoryToUse + ". Using JavassistImplFactory.");
            } catch (InstantiationException e) {
                useDefault = true;
                System.out.println("SREngine: could not instance the Class " + mapperToUse + ". Using JavassistImplementationFactory.");
            } catch (IllegalAccessException e) {
                useDefault = true;
                e.printStackTrace();
            }
        } else {
            useDefault = true;
        }
        if (useDefault) {
            implementationFactory = new JavassistImplFactory();
        }
        useDefault = false;
        String instanceFactoryToUse = SRPropManager.getString(SRPropManager.INSTANCE_FACTORY_TO_USE);
        if (null != instanceFactoryToUse) {
            try {
                Class instanceFactoryToUseClass = Class.forName(instanceFactoryToUse);
                instanceFactory = (InstanceFactory) instanceFactoryToUseClass.newInstance();
            } catch (ClassNotFoundException e) {
                useDefault = true;
                System.out.println("SREngine: could not find the Class " + mapperToUse + ". Using SommerInstanceFactory.");
            } catch (InstantiationException e) {
                useDefault = true;
                System.out.println("SREngine: could not instance the Class " + instanceFactoryToUse + ". Using SommerInstanceFactory.");
            } catch (IllegalAccessException e) {
                useDefault = true;
                e.printStackTrace();
            }
        } else {
            useDefault = true;
        }
        if (useDefault) {
            instanceFactory = new SommerInstanceFactory();
        }
        useDefault = false;
        String rdfaReaderToUse = SRPropManager.getString(SRPropManager.RDFS_READER);
        if (null != rdfaReaderToUse) {
            try {
                Class rdfaReaderToUseClass = Class.forName(rdfaReaderToUse);
                rdfaReader = (RdfaReader) rdfaReaderToUseClass.newInstance();
            } catch (ClassNotFoundException e) {
                useDefault = true;
                System.out.println("SREngine: could not find the Class " + rdfaReaderToUse + ". Using JenaRdfaReader.");
            } catch (InstantiationException e) {
                useDefault = true;
                System.out.println("SREngine: could not instance the Class " + rdfaReaderToUse + ". Using JenaRdfaReader.");
            } catch (IllegalAccessException e) {
                useDefault = true;
                e.printStackTrace();
            }
        } else {
            useDefault = true;
        }
        if (useDefault) {
            rdfaReader = new JenaRdfaReader();
        }
        useDefault = false;
    }

    /**
	 * @return
	 * @uml.property name="instance"
	 */
    public static SREngine getInstance() {
        if (null == instance) {
            return new SREngine();
        } else {
            return instance;
        }
    }

    /**
	 * @return
	 * @uml.property name="uriMapper"
	 */
    public UriMapper getUriMapper() {
        return uriMapper;
    }

    /**
	 * @param uriMapping
	 * @uml.property name="uriMapper"
	 */
    public void setUriMapper(UriMapper uriMapping) {
        this.uriMapper = uriMapping;
    }

    public ImplementationFactory getImplementationFactory() {
        return this.implementationFactory;
    }

    public InstanceFactory getInstanceFactory() {
        return this.instanceFactory;
    }

    public RdfaReader getRdfaReader() {
        return this.rdfaReader;
    }
}
