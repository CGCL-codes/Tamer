package com.csol.chem.mm3.patterns;

import com.csol.chem.util.pattern.PatternAtom;
import com.csol.chem.util.pattern.PatternMolecule;

/**
 * MM3 AutoType utility class,
 * used to change a sulphate group to methyl group so that mm3 can
 * autotype the rest of the system. Then changes it back and sets the
 * types correctly.
 * @author jiro
 *
 */
public class Sulphate extends MM3TypePattern {

    /**
     * 
     */
    public Sulphate() {
        super();
    }

    /**
     * @see com.csol.chem.mm3.patterns.MM3TypePattern#setup()
     */
    @Override
    public void setup() {
        this.correct = new PatternMolecule();
        PatternAtom correctS = new PatternAtom("S");
        PatternAtom correctO1 = new PatternAtom("O");
        PatternAtom correctO2 = new PatternAtom("O");
        PatternAtom correctO3 = new PatternAtom("O");
        correctS.bindPatternAtom(correctO1);
        correctS.bindPatternAtom(correctO2);
        correctS.bindPatternAtom(correctO3);
        correctS.setOkNrBonds(4);
        this.correct.addPatternAtom(correctS);
        this.correct.addPatternAtom(correctO1);
        this.correct.addPatternAtom(correctO2);
        this.correct.addPatternAtom(correctO3);
        this.changed = new PatternMolecule();
        PatternAtom changedS = new PatternAtom("C");
        PatternAtom changedO1 = new PatternAtom("H");
        PatternAtom changedO2 = new PatternAtom("H");
        PatternAtom changedO3 = new PatternAtom("H");
        changedS.bindPatternAtom(changedO1);
        changedS.bindPatternAtom(changedO2);
        changedS.bindPatternAtom(changedO3);
        changedS.setOkNrBonds(4);
        this.changed.addPatternAtom(changedS);
        this.changed.addPatternAtom(changedO1);
        this.changed.addPatternAtom(changedO2);
        this.changed.addPatternAtom(changedO3);
        this.toChanged.put(correctS, changedS);
        this.toChanged.put(correctO1, changedO1);
        this.toChanged.put(correctO2, changedO2);
        this.toChanged.put(correctO3, changedO3);
        this.toCorrect.put(changedS, correctS);
        this.toCorrect.put(changedO1, correctO1);
        this.toCorrect.put(changedO2, correctO2);
        this.toCorrect.put(changedO3, correctO3);
        correctTypes.put(correctS, 110);
        correctTypes.put(correctO1, 6);
        correctTypes.put(correctO2, 6);
        correctTypes.put(correctO3, 7);
        changedTypes.put(changedS, 1);
        changedTypes.put(changedO1, 5);
        changedTypes.put(changedO2, 5);
        changedTypes.put(changedO3, 5);
    }
}
