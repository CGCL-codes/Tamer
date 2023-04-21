package edu.ucsd.ncmir.unys;

import edu.ucsd.ncmir.visualizer.viewer.VisualizerShutdownNotifier;

class Closer implements VisualizerShutdownNotifier {

    public void shutdown() {
        System.exit(0);
    }
}
