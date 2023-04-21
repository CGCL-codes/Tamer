package org.dyndns.fichtner.theoryofsets.ant.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dyndns.fichtner.theoryofsets.ant.TheoryOfSetsUtil;

/**
 * Abstract base class for set theory functionality.
 * 
 * @author Peter Fichtner
 */
public abstract class AbstractTheoryOfSetsTask extends Task {

    private String property;

    private String delimiter = ",";

    private String setA, setB;

    private boolean sort;

    /**
	 * Returns the property name to set.
	 * 
	 * @return property name to set
	 */
    public String getProperty() {
        return this.property;
    }

    /**
	 * Sets the property name to set.
	 * 
	 * @param property property name to set
	 */
    public void setProperty(final String property) {
        this.property = property;
    }

    /**
	 * Returns the delimiter to use.
	 * 
	 * @return delimiter to use
	 */
    public String getDelimiter() {
        return this.delimiter;
    }

    /**
	 * Sets the delimiter to use.
	 * 
	 * @param delimiter delimiter to use
	 */
    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    /**
	 * Returns the first set (set A).
	 * 
	 * @return the first set (set A).
	 */
    public String getSetA() {
        return this.setA;
    }

    /**
	 * Sets the first set (set A).
	 * 
	 * @param setA the first set (set A).
	 */
    public void setSetA(final String setA) {
        this.setA = setA;
    }

    /**
	 * Returns the second set (set B).
	 * 
	 * @return the second set (set B).
	 */
    public String getSetB() {
        return this.setB;
    }

    /**
	 * Sets the second set (set B).
	 * 
	 * @param setB the second set (set B).
	 */
    public void setSetB(final String setB) {
        this.setB = setB;
    }

    /**
	 * Returns flag whether the output should be sorted or not.
	 * 
	 * @return <code>true</code> if output should be sorted
	 */
    public boolean isSort() {
        return this.sort;
    }

    /**
	 * Sets if the output should be sorted.
	 * 
	 * @param sort flag if output should be sorted
	 */
    public void setSort(final boolean sort) {
        this.sort = sort;
    }

    /**
	 * Nested element support for set A.
	 * 
	 * @param svh instance holding an element for set A
	 */
    public void addConfiguredSetA(final SetValueHolder svh) {
        this.setA = append(this.setA, svh);
    }

    /**
	 * Nested element support for set B.
	 * 
	 * @param svh instance holding an element for set B
	 */
    public void addConfiguredSetB(final SetValueHolder svh) {
        this.setB = append(this.setB, svh);
    }

    private String append(final String setVal, final SetValueHolder svh) {
        final String appVal = svh.getValue();
        return isEmpty(setVal) ? appVal : setVal + this.delimiter + appVal;
    }

    private boolean isEmpty(final String setVal) {
        return setVal == null || setVal.length() == 0;
    }

    @Override
    public void execute() throws BuildException {
        if (this.property == null) {
            throw new BuildException("property not defined");
        }
        if (this.delimiter == null) {
            throw new BuildException("delimiter not defined");
        }
        if (this.setA == null) {
            throw new BuildException("setA not defined");
        }
        if (this.setB == null) {
            throw new BuildException("setB not defined");
        }
        getProject().setProperty(getProperty(), TheoryOfSetsUtil.toString(sort(new HashSet<String>(calculate(TheoryOfSetsUtil.toSet(getSetA(), getDelimiter()), TheoryOfSetsUtil.toSet(getSetB(), getDelimiter())))), getDelimiter()));
    }

    protected abstract Collection<String> calculate(Set<String> setA, Set<String> setB);

    private Collection<String> sort(final Set<String> set) {
        if (!this.sort) {
            return set;
        }
        final List<String> result = new ArrayList<String>(set);
        Collections.sort(result);
        return result;
    }
}
