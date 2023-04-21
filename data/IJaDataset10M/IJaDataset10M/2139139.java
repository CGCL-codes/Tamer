package com.ivis.xprocess.ui.processdesigner.diagram.model;

public interface Folder extends Node {

    Metrics getMetrics();

    class Metrics {

        public int numTasks;

        public int numCategories;

        public Metrics(int numTasks, int numCategories) {
            this.numTasks = numTasks;
            this.numCategories = numCategories;
        }
    }
}
