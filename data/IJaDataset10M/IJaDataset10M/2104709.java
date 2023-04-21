package org.openscience.cdk.modeling.builder3d;

import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 *  Class stores hose code patterns to identify mm2 force field atom types.
 *
 * @author      chhoppe
 * @cdk.created 2004-09-07
 * @cdk.module  forcefield
 * @cdk.githash
 */
public class MM2BasedAtomTypePattern {

    private List<Pattern> atomTypePatterns = new Vector<Pattern>();

    /**
	 *Constructor for the MM2BasedAtomTypePattern object
	 */
    MM2BasedAtomTypePattern() {
        this.createPattern();
    }

    /**
	 *  Gets the atomTypePatterns attribute of the MM2BasedAtomTypePattern object
	 *
	 * @return    The atomTypePatterns as a vector
	 */
    public List<Pattern> getAtomTypePatterns() {
        return atomTypePatterns;
    }

    /**
	 *  Creates the atom type pattern
	 */
    private void createPattern() {
        atomTypePatterns.add(Pattern.compile("[CSP]-[0-4][-]?+;[A-Za-z+-]{0,6}[(].*+"));
        atomTypePatterns.add(Pattern.compile("[CS]-[0-3];[H]{0,2}+[A-Za-z]*+=[A-Z]{1,2}+.*+"));
        atomTypePatterns.add(Pattern.compile("C-[0-3];=O.*+"));
        atomTypePatterns.add(Pattern.compile("C-[1-2][-]?+;[H]{0,1}+%.*+"));
        atomTypePatterns.add(Pattern.compile("H-[0-1];[C].*+"));
        atomTypePatterns.add(Pattern.compile("[OS]-[0-2][-]?+;[A-Za-z]{1,4}+[+]?+[(].*+"));
        atomTypePatterns.add(Pattern.compile("O-[1-2][+]?+;[H]{0,1}+=[SPC].[^O]++.*+"));
        atomTypePatterns.add(Pattern.compile("N-[0-3][+-]?+;[A-Z &&[^=%]]{1,3}+.*+"));
        atomTypePatterns.add(Pattern.compile("N-[1-3][-+]?+;=?+[ON]?+[+]?+[CH]*+.(=O)?+.*+"));
        atomTypePatterns.add(Pattern.compile("N-[1-2][+]?+;%.*+"));
        atomTypePatterns.add(Pattern.compile("F.*+"));
        atomTypePatterns.add(Pattern.compile("Cl.*+"));
        atomTypePatterns.add(Pattern.compile("Br.*+"));
        atomTypePatterns.add(Pattern.compile("I.*+"));
        atomTypePatterns.add(Pattern.compile("S-[1-2][-]?+;[HCSON]{1,2}+[(].*+"));
        atomTypePatterns.add(Pattern.compile("S-3+;.?+[A-Za-z]++.*+"));
        atomTypePatterns.add(Pattern.compile("S-[1-2][+]?+;=[OCNP][A-Z]++.*+"));
        atomTypePatterns.add(Pattern.compile("S-4;=O=O[A-Za-z]++.*+"));
        atomTypePatterns.add(Pattern.compile("Si.*+"));
        atomTypePatterns.add(Pattern.compile("LP.*+"));
        atomTypePatterns.add(Pattern.compile("H-1;O[+-]?+.[PSCN]{0,2}+/.*+"));
        atomTypePatterns.add(Pattern.compile("C-3;CCC..?+&?+[A-Za-z]?+,?+.?+&?+,?+.?+&?+.*+"));
        atomTypePatterns.add(Pattern.compile("H-1;[NP][+]?+[(][H]{0,2}+=?+[A-Z]{0,2}+/.*+"));
        atomTypePatterns.add(Pattern.compile("H-1;O[+]?+.=?+C/=?+[OCSP]{1,2}+/.*+"));
        atomTypePatterns.add(Pattern.compile("P-[0-3];[A-Za-z]{1,3}[(].*+"));
        atomTypePatterns.add(Pattern.compile("B-[0-3];[A-Za-z]{1,2}.*+"));
        atomTypePatterns.add(Pattern.compile("B-4;[A-Za-z]{1,4}.*+"));
        atomTypePatterns.add(Pattern.compile("SPECIAL DEFINITON "));
        atomTypePatterns.add(Pattern.compile("NOT Implemented"));
        atomTypePatterns.add(Pattern.compile("C-[0-9][+];.*+"));
        atomTypePatterns.add(Pattern.compile("Ge.*+"));
        atomTypePatterns.add(Pattern.compile("Sn.*+"));
        atomTypePatterns.add(Pattern.compile("Pb.*+"));
        atomTypePatterns.add(Pattern.compile("Se.*+"));
        atomTypePatterns.add(Pattern.compile("Te.*+"));
        atomTypePatterns.add(Pattern.compile("D-1;.*+"));
        atomTypePatterns.add(Pattern.compile("N-2;=CC..*+"));
        atomTypePatterns.add(Pattern.compile("C-2;=CC..?+[A-Za-z]?+,?+&?+,?+C?+&?+.*+"));
        atomTypePatterns.add(Pattern.compile("N-4[+]?+;.*+"));
        atomTypePatterns.add(Pattern.compile("N-[2-3];H?+CC.[^(=O)].*+"));
        atomTypePatterns.add(Pattern.compile("O-2;CC.=C.*+&.*+&.*+"));
        atomTypePatterns.add(Pattern.compile("S-2;CC.*+"));
        atomTypePatterns.add(Pattern.compile("N-[2-3][+]?+;=N.*+C?+O?+[-]?+.*+"));
        atomTypePatterns.add(Pattern.compile("H-1;S.*+"));
        atomTypePatterns.add(Pattern.compile("N-2[+];=?+%?+[NC][-=]{0,2}+[NC][-]?+.*+"));
        atomTypePatterns.add(Pattern.compile("N-3[+];=O[A-Z]-?+[A-Z]-?+.*+"));
        atomTypePatterns.add(Pattern.compile("O-1-?+;=?+[CS][(][=0]?+[OCSNH]*+/.*+"));
        atomTypePatterns.add(Pattern.compile("H-1;N[+].[A-Z]{0,3}+/.*+"));
        atomTypePatterns.add(Pattern.compile("O-2;CC.H?+,?+H?+,?+&,&.*+"));
        atomTypePatterns.add(Pattern.compile("C-2;=CC.*+"));
        atomTypePatterns.add(Pattern.compile("He.*+"));
        atomTypePatterns.add(Pattern.compile("Ne.*+"));
        atomTypePatterns.add(Pattern.compile("Ar.*+"));
        atomTypePatterns.add(Pattern.compile("Kr.*+"));
        atomTypePatterns.add(Pattern.compile("Xe.*+"));
        atomTypePatterns.add(Pattern.compile("NotImplemented"));
        atomTypePatterns.add(Pattern.compile("NotImplemented"));
        atomTypePatterns.add(Pattern.compile("NotImplemented"));
        atomTypePatterns.add(Pattern.compile("Mg.*+"));
        atomTypePatterns.add(Pattern.compile("P-[2-4];.*"));
        atomTypePatterns.add(Pattern.compile("Fe.*+"));
        atomTypePatterns.add(Pattern.compile("Fe.*+"));
        atomTypePatterns.add(Pattern.compile("Ni.*+"));
        atomTypePatterns.add(Pattern.compile("Ni.*+"));
        atomTypePatterns.add(Pattern.compile("Co.*+"));
        atomTypePatterns.add(Pattern.compile("Co.*+"));
        atomTypePatterns.add(Pattern.compile("NotImplemented"));
        atomTypePatterns.add(Pattern.compile("NotImplemented"));
        atomTypePatterns.add(Pattern.compile("O-1[-]?+;=?+N.*+"));
        atomTypePatterns.add(Pattern.compile("O-3[+];[H]{0,3}+[C]{0,3}+[(].*+"));
        atomTypePatterns.add(Pattern.compile("C-1NotImplemented"));
        atomTypePatterns.add(Pattern.compile("N-2;=C[^CO].*+"));
        atomTypePatterns.add(Pattern.compile("N-3[+];[H]{0,2}+=?+[C]{0,3}+[(].*+"));
        atomTypePatterns.add(Pattern.compile("N-[2-3][+];=C[CO]{2}+.?+[(].*+"));
        atomTypePatterns.add(Pattern.compile("N-[2-3][+]?+;=CO.*+"));
        atomTypePatterns.add(Pattern.compile("H-1;N[(]{1}+[CH]{2,2}+/[H]{0,3}+[,]?+=OC.*+"));
        atomTypePatterns.add(Pattern.compile("H-1;O.C/=CC/.*+"));
        atomTypePatterns.add(Pattern.compile("N-[1-3];[CH]{1,3}.{1}+[A-Z]{0,3}+[,]?+=OC.*+"));
    }
}
