package org.lindenb.bio;

public class AminAcidUtils extends BioUtils {

    public static String to3letterCode(char aa) {
        switch(Character.toUpperCase(aa)) {
            case 'A':
                return "Ala";
            case 'C':
                return "Cys";
            case 'D':
                return "Asp";
            case 'E':
                return "Glu";
            case 'F':
                return "Phe";
            case 'G':
                return "Gly";
            case 'H':
                return "His";
            case 'I':
                return "Ile";
            case 'K':
                return "Lys";
            case 'L':
                return "Leu";
            case 'M':
                return "Met";
            case 'N':
                return "Asn";
            case 'P':
                return "Pro";
            case 'Q':
                return "Gln";
            case 'R':
                return "Arg";
            case 'S':
                return "Ser";
            case 'T':
                return "Thr";
            case 'V':
                return "Val";
            case 'W':
                return "Trp";
            case 'Y':
                return "Tyr";
            case '?':
            case '*':
                return "***";
        }
        throw new IllegalArgumentException("Bad amino acid " + aa);
    }

    /**
 *  AAindex FASG760101 - Molecular weight (Fasman, 1976)
 * Fasman, G.D., ed.
 * Handbook of Biochemistry and Molecular Biology", 3rd ed.,
 * Proteins - Volume 1, CRC Press, Cleveland (1976)
 * @param aa
 * @return
 */
    public static float getAminoAcidWeight(char aa) {
        switch(Character.toUpperCase(aa)) {
            case 'A':
                return 89.09f;
            case 'C':
                return 121.15f;
            case 'D':
                return 133.10f;
            case 'E':
                return 147.13f;
            case 'F':
                return 165.19f;
            case 'G':
                return 75.07f;
            case 'H':
                return 155.16f;
            case 'I':
                return 131.17f;
            case 'K':
                return 146.19f;
            case 'L':
                return 131.17f;
            case 'M':
                return 149.21f;
            case 'N':
                return 132.12f;
            case 'P':
                return 115.13f;
            case 'Q':
                return 146.15f;
            case 'R':
                return 174.20f;
            case 'S':
                return 105.09f;
            case 'T':
                return 119.12f;
            case 'U':
                return 168.06f;
            case 'V':
                return 117.15f;
            case 'W':
                return 204.23f;
            case 'Y':
                return 181.19f;
        }
        throw new IllegalArgumentException("Bad amino acid " + aa);
    }
}
