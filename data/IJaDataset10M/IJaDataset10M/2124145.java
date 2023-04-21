package edu.ucdavis.genomics.metabolomics.binbase.quantificator.types;

import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentSample;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentClass;

/**
 * User: wohlgemuth
 * Date: Sep 10, 2009
 * Time: 9:21:57 AM
 */
public class QuantificatorExperimentClass extends ExperimentClass {

    public void addSample(String name, double concentration) {
        ExperimentSample sample = new QuantificatorExperimentSample(concentration);
        sample.setId(name);
        sample.setName(name);
        if (this.getSamples() == null) {
            ExperimentSample[] s = new ExperimentSample[1];
            s[0] = sample;
            this.setSamples(s);
        } else {
            ExperimentSample[] s = new ExperimentSample[this.getSamples().length + 1];
            int counter = 0;
            for (ExperimentSample si : this.getSamples()) {
                s[counter] = si;
                counter++;
            }
            s[counter] = sample;
            this.setSamples(s);
        }
    }

    public void removeSample(String name) {
        boolean found = false;
        for (ExperimentSample sample : this.getSamples()) {
            System.out.println(sample.getName() + " - " + name);
            if (sample.getName().equals(name)) {
                found = true;
            }
        }
        System.out.println(found);
        if (found) {
            ExperimentSample[] samples = new ExperimentSample[this.getSamples().length - 1];
            int counter = 0;
            for (int i = 0; i < this.getSamples().length; i++) {
                if (this.getSamples()[i].getName().equals(name)) {
                } else {
                    samples[counter] = this.getSamples()[i];
                    counter++;
                }
            }
            this.setSamples(samples);
        }
    }
}
