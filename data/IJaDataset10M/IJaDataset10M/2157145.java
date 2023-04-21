package org.lindenb.bio;

public abstract class GeneticCode {

    private static final char STOP_CODON = '*';

    private static final GeneticCode standard = new StdGeneticCode();

    public abstract String getName();

    public abstract char translate(char a, char b, char c);

    public boolean isStopCodon(char c) {
        return c == STOP_CODON;
    }

    public static GeneticCode getStandard() {
        return standard;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        char bases[] = { 'A', 'T', 'G', 'C' };
        for (char base1 : bases) {
            for (char base2 : bases) {
                for (char base3 : bases) {
                    char c = this.translate(base1, base2, base3);
                    result = prime * result + c;
                }
            }
        }
        return result;
    }

    /** two GC are equals if they translate the same thing */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof GeneticCode)) return false;
        GeneticCode other = GeneticCode.class.cast(obj);
        char bases[] = { 'A', 'T', 'G', 'C' };
        for (char base1 : bases) {
            for (char base2 : bases) {
                for (char base3 : bases) {
                    if (this.translate(base1, base2, base3) != other.translate(base1, base2, base3)) return false;
                }
            }
        }
        return true;
    }

    /** NCBI takes a gc string from the NCBI */
    public static class NCBI extends GeneticCode {

        private String geneticCode;

        private String name;

        /**
		 * 
		 * @param name name of the GC
		 * @param geneticCode a string from http://www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi
		 */
        public NCBI(String name, String geneticCode) {
            this.name = name;
            if (this.name == null) {
                throw new IllegalArgumentException("name missing for this organism");
            }
            this.geneticCode = geneticCode;
            if (this.geneticCode == null || this.geneticCode.length() != 64) {
                throw new IllegalArgumentException("Expected a String of 64 chars:" + this.geneticCode);
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public char translate(char a, char b, char c) {
            int base1 = base2index(a);
            int base2 = base2index(b);
            int base3 = base2index(c);
            if (base1 == -1 || base2 == -1 || base3 == -1) {
                return '?';
            } else {
                return this.geneticCode.charAt(base1 * 16 + base2 * 4 + base3);
            }
        }

        private static int base2index(char c) {
            switch(Character.toLowerCase(c)) {
                case 't':
                    return 0;
                case 'c':
                    return 1;
                case 'a':
                    return 2;
                case 'g':
                    return 3;
                default:
                    return -1;
            }
        }
    }

    private static class StdGeneticCode extends GeneticCode {

        @Override
        public String getName() {
            return "Standard genetic code";
        }

        /**
	     * overides/implements parent
	     * 
	     */
        @Override
        public char translate(char b1, char b2, char b3) {
            char c;
            char c1 = Character.toLowerCase(b1);
            char c2 = Character.toLowerCase(b2);
            char c3 = Character.toLowerCase(b3);
            switch(c1) {
                case 'a':
                    switch(c2) {
                        case 'a':
                            switch(c3) {
                                case 'a':
                                    c = 'K';
                                    break;
                                case 't':
                                    c = 'N';
                                    break;
                                case 'g':
                                    c = 'K';
                                    break;
                                case 'c':
                                    c = 'N';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 't':
                            switch(c3) {
                                case 'a':
                                    c = 'I';
                                    break;
                                case 't':
                                    c = 'I';
                                    break;
                                case 'g':
                                    c = 'M';
                                    break;
                                case 'c':
                                    c = 'I';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'g':
                            switch(c3) {
                                case 'a':
                                    c = 'R';
                                    break;
                                case 't':
                                    c = 'S';
                                    break;
                                case 'g':
                                    c = 'R';
                                    break;
                                case 'c':
                                    c = 'S';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'c':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'T';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        default:
                            c = '?';
                            break;
                    }
                    break;
                case 't':
                    switch(c2) {
                        case 'a':
                            switch(c3) {
                                case 'a':
                                    c = STOP_CODON;
                                    break;
                                case 't':
                                    c = 'Y';
                                    break;
                                case 'g':
                                    c = STOP_CODON;
                                    break;
                                case 'c':
                                    c = 'Y';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 't':
                            switch(c3) {
                                case 'a':
                                    c = 'L';
                                    break;
                                case 't':
                                    c = 'F';
                                    break;
                                case 'g':
                                    c = 'L';
                                    break;
                                case 'c':
                                    c = 'F';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'g':
                            switch(c3) {
                                case 'a':
                                    c = STOP_CODON;
                                    break;
                                case 't':
                                    c = 'C';
                                    break;
                                case 'g':
                                    c = 'W';
                                    break;
                                case 'c':
                                    c = 'C';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'c':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'S';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        default:
                            c = '?';
                            break;
                    }
                    break;
                case 'g':
                    switch(c2) {
                        case 'a':
                            switch(c3) {
                                case 'a':
                                    c = 'E';
                                    break;
                                case 't':
                                    c = 'D';
                                    break;
                                case 'g':
                                    c = 'E';
                                    break;
                                case 'c':
                                    c = 'D';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 't':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'V';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'g':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'G';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'c':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'A';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        default:
                            c = '?';
                            break;
                    }
                    break;
                case 'c':
                    switch(c2) {
                        case 'a':
                            switch(c3) {
                                case 'a':
                                    c = 'Q';
                                    break;
                                case 't':
                                    c = 'H';
                                    break;
                                case 'g':
                                    c = 'Q';
                                    break;
                                case 'c':
                                    c = 'H';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 't':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'L';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'g':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'R';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        case 'c':
                            switch(c3) {
                                case 'a':
                                case 't':
                                case 'g':
                                case 'c':
                                    c = 'P';
                                    break;
                                default:
                                    c = '?';
                                    break;
                            }
                            break;
                        default:
                            c = '?';
                            break;
                    }
                    break;
                default:
                    c = '?';
                    break;
            }
            return c;
        }
    }
}
