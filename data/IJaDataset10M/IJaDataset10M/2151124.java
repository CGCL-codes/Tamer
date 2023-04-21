package org.xith3d.scenegraph.particles.jops;

import java.util.ArrayList;
import java.util.List;
import org.xith3d.scenegraph.GroupNode;
import org.softmed.jops.Generator;
import org.softmed.jops.ParticleSystem;
import org.softmed.jops.modifiers.Modifier;
import org.softmed.jops.modifiers.PointMass;

/**
 * Insert comment here.
 * 
 * @author Guilherme Gomes (aka guilhermegrg)
 */
public class GeneratorAndPointMassVisualizer {

    private List<PointMassNode> pointMasses = new ArrayList<PointMassNode>();

    private List<GeneratorNode> generatorNodes = new ArrayList<GeneratorNode>();

    private GroupNode node;

    public void dispose() {
        if (node == null) return;
        removeGeneratorsAndPointMasses();
        node = null;
    }

    protected void showGeneratorsAndPointMasses(ParticleSystem particleSystem, GroupNode node) {
        this.node = node;
        if (particleSystem == null || node == null) return;
        List<Generator> gens2 = particleSystem.getGenerators();
        for (int i = 0; i < gens2.size(); i++) {
            final Generator generator = gens2.get(i);
            GeneratorNode gnode = new GeneratorNode();
            gnode.setGenerator(generator);
            generatorNodes.add(gnode);
            node.addChild(gnode);
        }
        List<Modifier> modifiers = particleSystem.getModifiers();
        for (int i = 0; i < modifiers.size(); i++) {
            final Modifier modifier = modifiers.get(i);
            if (modifier instanceof PointMass) {
                PointMassNode pmnode = new PointMassNode();
                pmnode.setPointMass((PointMass) modifier);
                pointMasses.add(pmnode);
                node.addChild(pmnode);
            }
        }
    }

    protected void removeGeneratorsAndPointMasses() {
        for (int i = 0; i < generatorNodes.size(); i++) {
            final GeneratorNode generator = generatorNodes.get(i);
            node.removeChild(generator);
            generator.setGenerator(null);
        }
        generatorNodes.clear();
        for (int i = 0; i < pointMasses.size(); i++) {
            final PointMassNode pmnode = pointMasses.get(i);
            node.removeChild(pmnode);
            pmnode.setPointMass(null);
        }
        pointMasses.clear();
    }
}
