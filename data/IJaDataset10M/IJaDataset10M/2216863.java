package mimosa.probe;

import java.util.Collection;
import java.util.Vector;

/**
 * An output factory stores the descriptions of available probe observers. It is initialized with two
 * generalist probe observers.
 *
 * @author Jean-Pierre Muller
 */
public class OutputFactory {

    public static class OutputClasses {

        private Class<?> stateClass;

        private Class<?> visualizer;

        private Class<?> outputSpecification;

        private Class<?> outputEditor;

        /**
		 * 
		 * @param visualizer
		 * @param outputSpecification
		 * @param outputEditor
		 */
        public OutputClasses(Class<?> stateClass, Class<?> visualizer, Class<?> outputSpecification, Class<?> outputEditor) {
            this.stateClass = stateClass;
            this.visualizer = visualizer;
            this.outputSpecification = outputSpecification;
            this.outputEditor = outputEditor;
        }

        /**
		 * @return Returns the visualizer.
		 */
        public Class<?> getVisualizer() {
            return visualizer;
        }

        /**
		 * @return Returns the outputDescription.
		 */
        public Class<?> getOutputSpecification() {
            return outputSpecification;
        }

        /**
		 * @return Returns the outputEditor.
		 */
        public Class<?> getOutputEditor() {
            return outputEditor;
        }

        /**
		 * @return Returns the stateClass.
		 */
        public Class<?> getStateClass() {
            return stateClass;
        }

        /**
		 * @see java.lang.Object#toString()
		 */
        @Override
        public String toString() {
            return visualizer.getSimpleName();
        }
    }

    private static Collection<OutputClasses> visualizers = new Vector<OutputClasses>();

    /**
	 * Adds the visualizer class to the list of visualizers associated to the behavior class.
	 * @param behaviourClass the behavior class
	 * @param visualizerClass the visualizer class
	 */
    public static void addProbeOutput(Class<?> behaviourClass, Class<?> visualizerClass, Class<?> outputSpecification, Class<?> specificationEditor) {
        visualizers.add(new OutputClasses(behaviourClass, visualizerClass, outputSpecification, specificationEditor));
    }

    /**
	 * Retrieves the collection of visualizers associated to a behavior class
	 * @param behaviourClass The behavior class
	 * @return Returns the collection of visualizer class.
	 */
    public static Collection<OutputClasses> getProbeOutputs(Class<?> behaviourClass) {
        Collection<OutputClasses> visualizerList = new Vector<OutputClasses>();
        for (OutputClasses descr : visualizers) if (isSubClass(behaviourClass, descr.getStateClass())) visualizerList.add(descr);
        return visualizerList;
    }

    public static OutputClasses getProbeOutput(Class<?> behaviourClass, Class<?> visualizerClass) {
        for (OutputClasses descr : visualizers) if (behaviourClass == descr.getStateClass() && visualizerClass == descr.getVisualizer()) return descr;
        return null;
    }

    /**
	 * Tests whether class1 is equal or a subclass of class2.
	 * @param class1 The first class.
	 * @param class2 The second class.
	 * @return Returns true if it is the case, false otherwise.
	 */
    public static boolean isSubClass(Class<?> class1, Class<?> class2) {
        if (class1 == null) return false; else if (class1 == class2) return true; else return isSubClass(class1.getSuperclass(), class2);
    }

    /**
	 * Retrieves all the possible visualizers.
	 * @return Returns a collection of output descriptions.
	 */
    public static Collection<OutputClasses> getProbeOutputs() {
        return visualizers;
    }
}
