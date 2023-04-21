package eu.annocultor.core.api;

import eu.annocultor.core.context.Namespaces;
import eu.annocultor.core.impl.CoreFactory;
import eu.annocultor.xconverter.api.Environment;
import eu.annocultor.xconverter.api.Graph;
import eu.annocultor.xconverter.api.Namespace;

/**
 * Public factory. Should be used whenever possible.
 * 
 * @author Borys Omelayenko
 * 
 */
public class Factory {

    /**
	 * Makes a new conversion task.
	 * 
	 * @param signature
	 *          signature of the dataset, e.g. a museum name.
	 * @param subsignature
	 *          subsignature of the dataset, e.g. <code>works</code>,
	 *          <code>terms</code>, <code>artists</code>.
	 * @param description
	 *          description of the dataset that will appear in the output RDF
	 *          files
	 * @param targetNamespace
	 *          namespace of the dataset objects to appear in the output RDF files
	 */
    public static Task makeTask(String signature, String subsignature, String description, Namespace targetNamespace, Environment env) {
        return CoreFactory.makeTask(signature, subsignature, description, targetNamespace, env);
    }

    /**
	 * Makes a new converter given a task.
	 * 
	 * @param task
	 *          conversion task
	 * @param tester
	 *          tester with post-conversion tests
	 * @return
	 */
    public static ConverterKernel makeConverter(Task task, ConverterTester tester) {
        return CoreFactory.makeConverter(task, tester);
    }

    private static Graph ignoreTarget = null;

    /**
	 * Makes a graph that will be ignored and never written into.
	 */
    public static Graph graphToIgnore() {
        if (ignoreTarget == null) ignoreTarget = CoreFactory.makeGraph(Factory.makeTask("ignore", "task", "Task to ignore", Namespaces.ANNO, null), "works", "", false);
        return ignoreTarget;
    }

    public static boolean isGraphToIgnore(Graph graph) {
        return graph == graphToIgnore();
    }

    /**
	 * Makes a standard target for works.
	 */
    public static Graph makeWorksTarget(Task task, String... comment) {
        return CoreFactory.makeGraph(task, "works", "", true, comment);
    }

    /**
	 * Makes a standard target for images.
	 */
    public static Graph makeImagesTarget(Task task, String... comment) {
        return CoreFactory.makeGraph(task, "images", "", true, comment);
    }

    /**
	 * Makes a standard target for annotations (from loca thesaurus) of works.
	 */
    public static Graph makeLocalAnnotationsTarget(Task task, String... comment) {
        return CoreFactory.makeGraph(task, "annotations", "local", true, comment);
    }

    /**
	 * Makes a standard target for external annotations (not in local thesaurus)
	 * of works.
	 */
    public static Graph makeExtAnnotationsTarget(Task task, String... comment) {
        return CoreFactory.makeGraph(task, "annotations", "external", true, comment);
    }

    /**
	 * Makes a standard target for long descriptions of works.
	 */
    public static Graph makeDescriptionsGraph(Task task, String... comment) {
        return CoreFactory.makeGraph(task, "descriptions", "", true, comment);
    }

    /**
	 * Makes a standard target for terms.
	 */
    public static Graph makeTermsGraph(Task task, String... comment) {
        return CoreFactory.makeGraph(task, "terms", "", true, comment);
    }

    /**
	 * Makes a standard target for directory of people.
	 */
    public static Graph makePeopleGraph(Task task, String... comment) {
        return CoreFactory.makeGraph(task, "people", "", true, comment);
    }

    /**
	 * Makes a custom target.
	 */
    public static Graph makeGraph(Task task, String objectType, String propertyType, String... comment) {
        return CoreFactory.makeGraph(task, objectType, propertyType, true, comment);
    }
}
