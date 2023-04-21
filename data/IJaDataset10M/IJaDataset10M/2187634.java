package net.sf.dozer.functional_tests.recursive;

/**
 */
public class ClassBPrime {

    /** */
    private String rue;

    /** */
    private String ville;

    /** */
    private ClassAPrime parent;

    /** */
    private int prime = 31;

    /** {@inheritDoc} */
    public ClassAPrime getParent() {
        return this.parent;
    }

    /** {@inheritDoc} */
    public void setParent(final ClassAPrime parent) {
        this.parent = parent;
    }

    /** {@inheritDoc} */
    public String getRue() {
        return this.rue;
    }

    /** {@inheritDoc} */
    public void setRue(final String rue) {
        this.rue = rue;
    }

    /** {@inheritDoc} */
    public String getVille() {
        return this.ville;
    }

    /** {@inheritDoc} */
    public void setVille(final String ville) {
        this.ville = ville;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        int result = 1;
        result = prime * result + ((this.rue == null) ? 0 : this.rue.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ClassBPrime other = (ClassBPrime) obj;
        if (this.rue == null) {
            if (other.rue != null) {
                return false;
            }
        } else if (!this.rue.equals(other.rue)) {
            return false;
        }
        return true;
    }
}
