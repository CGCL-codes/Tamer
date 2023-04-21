package com.csol.chem.mm3.patterns;

import com.csol.chem.util.pattern.PatternAtom;
import com.csol.chem.util.pattern.PatternMolecule;

/**
 * AutoTyping utility, used for allowing auto typing of systems that has
 * carboxylate ions. temporarily changes one oxygen to a hydrogen,
 * then changes back after typing and sets the types right.
 * @author jiro
 *
 */
public class Carboxylate extends MM3TypePattern {

    /**
     * 
     */
    public Carboxylate() {
        super();
    }

    /**
     * @see com.csol.chem.mm3.patterns.MM3TypePattern#setup()
     */
    @Override
    public void setup() {
        this.correct = new PatternMolecule();
        PatternAtom correctC = new PatternAtom("C");
        PatternAtom correctO1 = new PatternAtom("O");
        PatternAtom correctO2 = new PatternAtom("O");
        correctC.bindPatternAtom(correctO1);
        correctC.bindPatternAtom(correctO2);
        correctC.setOkNrBonds(3);
        this.correct.addPatternAtom(correctC);
        this.correct.addPatternAtom(correctO1);
        this.correct.addPatternAtom(correctO2);
        this.changed = new PatternMolecule();
        PatternAtom changedC = new PatternAtom("C");
        PatternAtom changedO1 = new PatternAtom("O");
        PatternAtom changedH = new PatternAtom("H");
        changedC.bindPatternAtom(changedO1);
        changedC.bindPatternAtom(changedH);
        changedC.setOkNrBonds(3);
        this.changed.addPatternAtom(changedC);
        this.changed.addPatternAtom(changedO1);
        this.changed.addPatternAtom(changedH);
        this.toChanged.put(correctC, changedC);
        this.toChanged.put(correctO1, changedO1);
        this.toChanged.put(correctO2, changedH);
        this.toCorrect.put(changedC, correctC);
        this.toCorrect.put(changedO1, correctO1);
        this.toCorrect.put(changedH, correctO2);
        correctTypes.put(correctC, 3);
        correctTypes.put(correctO1, 47);
        correctTypes.put(correctO2, 47);
        changedTypes.put(changedC, 3);
        changedTypes.put(changedO1, 7);
        changedTypes.put(changedH, 5);
    }
}
