package org.openscience.cdk.renderer.generate;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.renderer.element.SymbolGroup;
import org.openscience.cdk.renderer.element.IRenderingElement;

public class BasicGenerator implements IGenerator {

    private AtomModule atomModule;

    private Renderer2DModel renderer2DModel;

    private BasicBondGenerator bondGenerator;

    public BasicGenerator(Renderer2DModel renderer2DModel) {
        this.renderer2DModel = renderer2DModel;
        this.bondGenerator = new BasicBondGenerator(renderer2DModel);
        this.atomModule = new AtomModule(renderer2DModel);
    }

    public Renderer2DModel getRenderer2DModel() {
        return this.renderer2DModel;
    }

    public IRenderingElement generate(IChemModel chemModel) {
        return null;
    }

    public SymbolGroup generate(IAtomContainer ac) {
        SymbolGroup symbolGroup = new SymbolGroup();
        if (ac == null) {
            return symbolGroup;
        }
        symbolGroup.add(bondGenerator.generate(ac));
        for (IAtom atom : ac.atoms()) {
            symbolGroup.add(this.atomModule.generate(ac, atom));
        }
        return symbolGroup;
    }
}
