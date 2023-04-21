package org.softmed.rest.server.guice;

import groovy.util.GroovyScriptEngine;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import org.softmed.action.guice.ActionModule;
import org.softmed.action.implementation.GroovyScriptActionStep;
import org.softmed.neodatis.persistence.impl.DefaultApplicationProvider;
import org.softmed.neodatis.persistence.impl.NeoDatisPersistenceFactory;
import org.softmed.neodatis.util.provider.ClientServerProviderManager;
import org.softmed.neodatis.util.provider.ServerWrapper;
import org.softmed.rest.admin.aop.manager.AspectProvider;
import org.softmed.rest.admin.guice.AdminModule;
import org.softmed.rest.generation.classmanager.ClassManager;
import org.softmed.rest.generation.guice.GenerationModule;
import org.softmed.rest.generation.handler.ExtendedHandlerSolutionProvider;
import org.softmed.rest.generation.jar.DynamicJarLoader;
import org.softmed.rest.generation.scafold.build.ApplicationProcessor;
import org.softmed.rest.generation.scafold.build.DirectoryReader;
import org.softmed.rest.generation.scafold.build.PersistenceProviderReader;
import org.softmed.rest.generation.solution.PersistenceFactory;
import org.softmed.rest.generation.xml.PagedList;
import org.softmed.rest.generation.xml.XMLConverterSuite;
import org.softmed.rest.generation.xml.manager.XMLSuiteManager;
import org.softmed.rest.server.config.Config;
import org.softmed.rest.server.core.boot.ApplicationProvider;
import org.softmed.rest.server.core.boot.BootupProcessor;
import org.softmed.rest.server.core.guice.CoreRESTServerModule;
import org.softmed.rest.server.core.guice.NamedAnnotation;
import org.softmed.rest.server.core.handler.SpecificHandlerSolver;
import org.softmed.rest.server.core.handler.impls.JavaSolver;
import org.softmed.rest.server.defaults.boot.DefaultBootupProcessor;
import org.softmed.rest.server.defaults.generation.DefaultAdminBuilder;
import org.softmed.rest.server.defaults.generation.DefaultApplicationConfigReader;
import org.softmed.rest.server.defaults.generation.DefaultClassReader;
import org.softmed.rest.server.defaults.generation.DefaultClassValidator;
import org.softmed.rest.server.defaults.generation.DefaultDirectoryReader;
import org.softmed.rest.server.defaults.generation.DefaultGenericConfigReader;
import org.softmed.rest.server.defaults.generation.DefaultHandlerSuiteReader;
import org.softmed.rest.server.defaults.generation.DefaultModuleBuilderReader;
import org.softmed.rest.server.defaults.generation.DefaultPersistenceProviderReader;
import org.softmed.rest.server.defaults.generation.DefaultPersistenceValidator;
import org.softmed.rest.server.defaults.generation.DefaultXMLSuiteReader;
import org.softmed.rest.server.defaults.handlers.ActionSolver;
import org.softmed.rest.server.defaults.handlers.GroovyClassSolver;
import org.softmed.rest.server.defaults.handlers.GroovySolver;
import org.softmed.rest.server.defaults.providers.DefaultAspectProvider;
import org.softmed.rest.server.xstream.GenericToXML;
import org.softmed.rest.server.xstream.PagedListConverter;
import org.softmed.rest.server.xstream.ProcessObjectEdit;
import org.softmed.rest.server.xstream.RecoverExistingObjectsUnmarshaller;
import org.softmed.rest.server.xstream.URIXMLEditor;
import org.softmed.rest.server.xstream.XStreamManager;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

public class RESTServerModule implements Module {

    String aspectProviderName = "aop-provider";

    String aspectModuleURI = "aop";

    String aspectPackage = "org.softmed.rest.admin.aop";

    String restAdminProviderName = "rest-admin-provider";

    String restAdminModuleURI = "rest";

    String restAdminPackage = "org.softmed.rest.config";

    private String[] roots;

    private Config cfg;

    public RESTServerModule(Config cfg, String[] roots) {
        this.roots = roots;
        this.cfg = cfg;
    }

    @Override
    public void configure(Binder binder) {
        GroovyScriptActionStep.addImport("org.restlet.data.Status");
        GroovyScriptActionStep.addImport("org.restlet.data.MediaType");
        binder.bind(PersistenceFactory.class).to(NeoDatisPersistenceFactory.class).asEagerSingleton();
        binder.bindConstant().annotatedWith(new NamedAnnotation("URIFIeldName")).to("uri");
        binder.bind(BootupProcessor.class).toInstance(new DefaultBootupProcessor());
        binder.requestStaticInjection(URIXMLEditor.class);
        binder.requestStaticInjection(RecoverExistingObjectsUnmarshaller.class);
        binder.requestStaticInjection(ProcessObjectEdit.class);
        CoreRESTServerModule coreRESTServerModule = new CoreRESTServerModule(cfg.getPort(), cfg.getPath(), cfg.getDomain(), false);
        coreRESTServerModule.configure(binder);
        String mainPath = coreRESTServerModule.getServer().getMainPath();
        binder.bind(DirectoryReader.class).to(DefaultDirectoryReader.class);
        binder.bind(PersistenceProviderReader.class).to(DefaultPersistenceProviderReader.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("ClassReader")).to(DefaultClassReader.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("ClassValidator")).to(DefaultClassValidator.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("GenericConfigReader")).to(DefaultGenericConfigReader.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("PersistenceValidator")).to(DefaultPersistenceValidator.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("XMLSuiteReader")).to(DefaultXMLSuiteReader.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("HandlerSuiteReader")).to(DefaultHandlerSuiteReader.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("ModuleBuilderReader")).to(DefaultModuleBuilderReader.class);
        binder.bind(ApplicationProcessor.class).annotatedWith(new NamedAnnotation("ApplicationConfigReader")).to(DefaultApplicationConfigReader.class);
        GenerationModule generationModule = new GenerationModule(mainPath, roots, null);
        generationModule.configure(binder);
        GroovyScriptEngine gse = null;
        try {
            URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            gse = new GroovyScriptEngine(roots, urlClassLoader);
            binder.bind(GroovyScriptEngine.class).toInstance(gse);
            binder.bind(SpecificHandlerSolver.class).annotatedWith(new NamedAnnotation("GroovyClassSolver")).to(GroovyClassSolver.class);
            binder.bind(SpecificHandlerSolver.class).annotatedWith(new NamedAnnotation("GroovySolver")).to(GroovySolver.class);
            binder.bind(SpecificHandlerSolver.class).annotatedWith(new NamedAnnotation("JavaSolver")).to(JavaSolver.class);
            binder.bind(SpecificHandlerSolver.class).annotatedWith(new NamedAnnotation("ActionSolver")).to(ActionSolver.class);
            binder.bind(new TypeLiteral<List<SpecificHandlerSolver>>() {
            }).toProvider(ExtendedHandlerSolutionProvider.class);
            ActionModule actionModule = new ActionModule(null, gse, null);
            actionModule.configure(binder);
            binder.requestStaticInjection(ClassManager.class);
            binder.requestStaticInjection(DynamicJarLoader.class);
            binder.bindConstant().annotatedWith(new NamedAnnotation("AspectProviderName")).to(aspectProviderName);
            binder.bindConstant().annotatedWith(new NamedAnnotation("AspectModuleURI")).to(aspectModuleURI);
            binder.bindConstant().annotatedWith(new NamedAnnotation("AspectPackage")).to(aspectPackage);
            binder.bindConstant().annotatedWith(new NamedAnnotation("RESTAdminProviderName")).to(restAdminProviderName);
            binder.bindConstant().annotatedWith(new NamedAnnotation("RestAdminModuleURI")).to(restAdminModuleURI);
            binder.bindConstant().annotatedWith(new NamedAnnotation("RestAdminPackage")).to(restAdminPackage);
            AdminModule adminModule = new AdminModule();
            adminModule.configure(binder);
            binder.requestStaticInjection(XStreamManager.class);
            XMLConverterSuite xmlSuite = new XMLConverterSuite();
            xmlSuite.setRecoverExistingObjects(new RecoverExistingObjectsUnmarshaller());
            xmlSuite.setProcessObjectEdit(new ProcessObjectEdit());
            GenericToXML.getConverters().put(PagedList.class, new PagedListConverter());
            xmlSuite.setGenericToXML(new GenericToXML());
            binder.bind(XMLConverterSuite.class).annotatedWith(new NamedAnnotation("DefaultXMLSuite")).toInstance(xmlSuite);
            binder.requestStaticInjection(XMLSuiteManager.class);
            org.neodatis.odb.Configuration.setClassLoader(actionModule.getGroovyClassLoader());
            org.neodatis.odb.Configuration.useMultiThread(true, 10);
            ServerWrapper defaultServer = new ServerWrapper();
            defaultServer.startupServer();
            ClientServerProviderManager.setDefaultServer(defaultServer);
            binder.requestStaticInjection(DefaultApplicationProvider.class);
            binder.bind(ApplicationProvider.class).to(DefaultApplicationProvider.class).asEagerSingleton();
            binder.requestStaticInjection(DefaultAspectProvider.class);
            binder.bind(AspectProvider.class).to(DefaultAspectProvider.class).asEagerSingleton();
            Map<String, String> sourceToDest = DefaultAdminBuilder.getSourceToDestination();
            sourceToDest.put("rest.odb", "apps/admin/modules/rest");
            sourceToDest.put("aop.odb", "apps/admin/modules/aop");
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
}
