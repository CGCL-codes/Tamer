package org.micthemodel.postProcessorPlugins;

import org.micthemodel.elements.Material;
import org.micthemodel.elements.ModelGrain;
import org.micthemodel.elements.Reactor;
import org.micthemodel.factory.Parameters;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.micthemodel.particles.ReactingParticle;

/**
 *
 * @author bishnoi
 */
public class LimitedLayerThicknessPlotter extends org.micthemodel.plugins.postProcessor.PostProcessor {

    ModelGrain model;

    int layerNumber;

    Material limitingMaterial;

    double minAmount;

    double maxAmount;

    File file;

    public LimitedLayerThicknessPlotter() {
    }

    public LimitedLayerThicknessPlotter(Reactor reactor, ModelGrain model, Material limitingMaterial, double minAmount, double maxAmount, int layerNumber) {
        super(reactor);
        this.model = model;
        this.layerNumber = layerNumber;
        this.limitingMaterial = limitingMaterial;
        this.minAmount = minAmount;
        if (maxAmount < 0) {
            maxAmount = Double.POSITIVE_INFINITY;
        }
        this.maxAmount = maxAmount;
        Object[] initialisationValues = { reactor, model, limitingMaterial, minAmount, maxAmount, layerNumber };
        this.constructorParameterValues = initialisationValues;
    }

    @Override
    public void process(double time, int step) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        double average = 0;
        double numberOfParticles = this.model.getGrainList().size();
        for (ReactingParticle grain : this.model.getGrainList()) {
            double amount = grain.getAmount(this.limitingMaterial);
            if (amount < this.minAmount || amount > this.maxAmount) {
                numberOfParticles--;
                continue;
            }
            double thickness = grain.layerThickness(this.layerNumber);
            if (thickness < min) {
                min = thickness;
            }
            if (thickness > max) {
                max = thickness;
            }
            average += thickness;
        }
        average /= numberOfParticles;
        if (time == 0) {
            this.initialiseFile();
        }
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(file, true));
            out.println(step + "," + time + "," + min + "," + max + "," + average);
            out.close();
        } catch (FileNotFoundException ex) {
            Parameters.getOut().println("Could not save thickness file for step " + step);
        }
    }

    private void initialiseFile() {
        File folder = new File(this.reactor.getParameters().getFileFolder() + "limitedThickness" + File.separator);
        if (!folder.exists()) {
            folder.mkdir();
        }
        this.file = new File(this.reactor.getParameters().getFileFolder() + "limitedThickness" + File.separator + "limitedThickness" + this.model.getName() + this.layerNumber + ".csv");
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(file));
            out.println("Step,Time,Minimum thickness,Maximum thickness,Average thickness");
            out.close();
        } catch (FileNotFoundException ex) {
            Parameters.getOut().println("Could not save thickness file");
        }
    }

    public Class[] constructorParameterClasses() {
        Class[] result = { Reactor.class, ModelGrain.class, Material.class, double.class, double.class, int.class };
        return result;
    }

    public String[] constructorParameterNames() {
        String[] result = { "Reactor", "Model grain to search around", "Limiting Material", "Minimum amount", "Maximum amount", "Layer number" };
        return result;
    }

    public Object[] constructorParameterValues() {
        return this.constructorParameterValues;
    }
}
