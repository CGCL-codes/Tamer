package jrelcal.labelledrelations;

/**
 * Abstract class Label, which implements Semiring
 *
 * B - set of the Semiring
 */
abstract class Label<B extends Comparable<B>> implements Comparable<Label<B>>, Semiring<Label<B>> {

    private B value;

    /**
	 * Label constructor.
	 * Sets the value of Label <tt>value</tt>.
	 * 
	 *  @param value
	 */
    public Label(B value) {
        this.value = value;
    }

    /**
	 * Returns the value of Label <tt>value</tt>.
	 * 
	 * @return <tt> B </tt> , the label value
	 */
    public B getValue() {
        return value;
    }

    /**
	 * Verifys if Label <tt>label</tt> is equals to Label <tt>value</tt>.
	 * 
	 * @param label
	 * @return true if the argument label its equal to the Label value
	 */
    public boolean equals(Label<B> label) {
        return value.equals(label.getValue());
    }

    /**
	 * Compares Label <tt>value</tt> to Label <tt>label</tt>
	 * 
	 * @param label
	 */
    public int compareTo(Label<B> label) {
        return value.compareTo(label.getValue());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
	 * Verifys if Label <tt>label</tt> is equals to Object <tt>obj</tt>.
	 * 
	 * @param label
	 * @return true if the argument obj is a Label and is equal to the Label value
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Label<?> other = (Label<?>) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
}
