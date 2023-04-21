package picoevo.ext.representation;

import java.util.ArrayList;
import picoevo.core.*;
import picoevo.core.evolution.*;
import picoevo.core.representation.*;
import picoevo.ext.*;
import picoevo.ext.evolution.*;
import picoevo.ext.representation.*;
import picoevo.toolbox.*;

public class Element_StaticArray_Double extends Element_Array {

    protected double[] _array;

    public Element_StaticArray_Double(Individual __individualOwner, ArrayList __elementLevelOperatorList) {
        super(__individualOwner, __elementLevelOperatorList);
    }

    public Element_StaticArray_Double(String __name, Individual __individualSpace, ArrayList __elementLevelOperatorList) {
        super(__name, __individualSpace, __elementLevelOperatorList);
    }

    public Element_StaticArray_Double(Individual __individualOwner, ArrayList __elementLevelOperatorList, int __size) {
        super(__individualOwner, __elementLevelOperatorList);
        this.setInitGenotypeLength(__size);
    }

    public Element_StaticArray_Double(String __name, Individual __individualSpace, ArrayList __elementLevelOperatorList, int __size) {
        super(__name, __individualSpace, __elementLevelOperatorList);
        this.setInitGenotypeLength(__size);
    }

    /**
	 * return a *copy* of bitList (i.e. *not* a pointer to the original list) - return value is a double[]
	 * @param __bitList
	 */
    public Object getArray() {
        if (this.getInitGenotypeLength() == -1) Display.error("" + this.getClass().getName() + "::getArray  - array not initialized");
        return ((double[]) this._array.clone());
    }

    /**
	 * copy array values into the internal array (genotype) - parameter should be a boolean[]
	 * @param __array
	 */
    public void setArray(Object __array) {
        try {
            if (this.getInitGenotypeLength() == -1) Display.error("" + this.getClass().getName() + "::setArray - array not initialized");
            if (this._array != null) if (this._array.length != ((double[]) __array).length) Display.error("" + this.getClass().getName() + "::setArray - array length do not match");
            this._array = (double[]) ((double[]) __array).clone();
        } catch (ClassCastException e) {
            Display.critical("" + this.getClass().getName() + "::setArray - Object does not match internal structure");
        }
    }

    public void displayInformation() {
        Display.info_nocr("( \"" + _name + "\",{");
        for (int i = 0; i != this._initGenotypeLength; i++) Display.info_nocr(" " + this._array[i]);
        Display.info_nocr("},");
        for (int i = 0; i != _variationOperatorList.size(); i++) ((VariationOperator) _variationOperatorList.get(i)).displayInformation();
        Display.info_nocr(" )");
    }
}
