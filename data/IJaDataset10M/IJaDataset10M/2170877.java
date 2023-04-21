package javastates;

import java.lang.reflect.Method;

/** 
 * Class requirement. Use this class when the api for field matches the Java beans conventions, and the field name begins
 * it an uppercase letter, potentially in situations where no actual data field exist in the target class, but only the "get/is/set"
 * api.
<p>JavaStates is <a href="http://javastates.sourceforge.net">a source forge project</a>, with <a href="http://sourceforge.net/projects/javastates/">further downloads and documentation</a>.
 
 * </p><p>JavaStates is developped at the <a href="http://www.esil.univmed.fr">Ecole Supérieure d'Ingénieurs de Luminy</a>, 
 * a school of the <a href="http://www.univmed.fr/">Universite de la Mediterranee</a>, Marseille, France.
 * </p><p>JavaStates is based upon original work by <a href="laurent.henocque.free.fr">laurent henocque</a>.
 * </p><p>JavaStates is released under the terms of the <a href="http://www.xfree86.org/3.3.6/COPYRIGHT2.html#5">modified BSD license</a>.
 * </p>
 <p class='copyright'>
 Copyright 2009-2010 Universite de la Mediterranee, Laurent Henocque<br />
 Copyright 2008 Universite de la Mediterranee, Laurent Henocque<br />
 Copyright 2007 Universite de la Mediterranee, Benjamin Batbie, Laurent Henocque, Mourad Mahouachi<br />
 Copyright 2005, 2006 Universite de la Mediterranee, Laurent Henocque<br />
 Copyright 2004 Universite de la Mediterranee, Laurent Henocque, Gregoire Petit dit Dariel, Bertrand Sautter<br />
 Copyright 1994-2003 Universite de la Mediterranee, Laurent Henocque<br />
 Copyright 1992-1993 Laurent Henocque<br />
 All rights reserved.
</p>
<p class='license'>
 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
<ul>
    <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.</li>
    <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.</li>
    <li>Neither the name of the Universite de la Mediterranee nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.</li>
</ul>
</p>
<p class='disclaimer'>
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
</p>
 * @author Laurent Henocque
 * @author Benjamen Batbie and Mourad Mahouachi 
 * @author Bertrand Sautter, Gregoire Petit dit Dariel 
 * 
 * 
 * @param targetObject The object which own the field
 * @param requiredValue The value to put in the field
 * @param storedValue The old value in the field
 * 
 * 
 * 
 * @since  1.0
 * @version  1.5 22/02/2008
 * @version 1.0 19/01/06
 */
public class ReqJavaBeansField implements Requirement {

    /**
	 * comparing two requirements
	 *
	 * @param r A requirement to value equal or not.
	 * 
	 * @return boolean : whether the two requirements 
	 * 
	 */
    @Override
    public boolean equals(Object r) {
        ReqJavaBeansField r2 = (ReqJavaBeansField) r;
        return (targetObject.equals(r2.targetObject) && fieldName.equals(r2.fieldName));
    }

    /**
 * 
 * Constructor
 * @param target the object to which the requirement applies.
 * @param field the field name.
 * @param value the value to set the field to.
 * 
 */
    public ReqJavaBeansField(Object target, String field, Object value) throws JavaBeansAccessorException {
        targetObject = target;
        requiredValue = value;
        fieldName = field;
        storedValue = null;
        assert targetObject != null : "missing target object";
        assert requiredValue != null : "missing required value";
        assert (fieldName != null && fieldName != "") : "missing field name";
        initGetterSetter();
    }

    /**
	 * Exception thrown when the accessors of a java bean field cannot be found
	 * @author lo
	 *
	 */
    class JavaBeansAccessorException extends Exception {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        /**
		 * constructor: takes the field name as a parameter
		 * @param fieldName : the field name
		 */
        JavaBeansAccessorException(String message) {
            super(message);
        }
    }

    /**
	 * a function to find the getters and setter according to the Java Beans conventions.
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws JavaBeansAccessorException 
	 */
    private void initGetterSetter() throws JavaBeansAccessorException {
        String is, get, set;
        is = new String("is" + fieldName);
        get = new String("get" + fieldName);
        set = new String("set" + fieldName);
        try {
            getter = targetObject.getClass().getMethod(get, (Class[]) null);
        } catch (Exception e) {
            try {
                getter = targetObject.getClass().getMethod(is, (Class[]) null);
            } catch (Exception f) {
                throw new JavaBeansAccessorException("Missing getter: field " + fieldName + " does not obey java beans naming policy.");
            }
        }
        assert getter != null : "Missing getter: field " + fieldName + " does not obey java beans naming policy.";
        try {
            setter = targetObject.getClass().getMethod(set, getter.getReturnType());
        } catch (Exception e) {
            throw new JavaBeansAccessorException("Missing setter: field " + fieldName + " does not obey java beans naming policy.");
        }
        assert setter != null : "Missing setter: field " + fieldName + " does not obey java beans naming policy.";
    }

    private void applyValue(Object o) throws Exception {
        assert o != null : "applying " + fieldName + " setter to null data ";
        if (o != null) setter.invoke(targetObject, new Object[] { o });
    }

    private void storeValue() throws Exception {
        if (storedValue == null) storedValue = getter.invoke(targetObject, (Object[]) null);
    }

    /**
	 * Applies the requirement.
	 * The old value is saved and the new value is setted. 
	 * @exception Exception
	 */
    public void apply() throws Exception {
        storeValue();
        applyValue(requiredValue);
    }

    /**
	 *
	 * Restores the old value of the field.. 
	 * @exception Exception
	 * 
	 *	
	 */
    public void restore() throws Exception {
        applyValue(storedValue);
        storedValue = null;
    }

    private Object targetObject;

    private Object requiredValue;

    private Object storedValue;

    private String fieldName;

    private Method getter;

    private Method setter;

    private static final long serialVersionUID = 1L;

    /**
 * 
 * @return this requirement's base field name
 */
    public String getFieldName() {
        return fieldName;
    }

    /**
 * 
 * @return this requirement's required value
 */
    public Object getRequiredValue() {
        return requiredValue;
    }

    /**
 * 
 * @return the value stored by the requirement. This is the value present for the resource *before* the requirement was applied.
 */
    public Object getStoredValue() {
        return storedValue;
    }

    /**
 * 
 * @return the object this requirement applies to
 */
    public Object getTargetObject() {
        return targetObject;
    }

    @Override
    public String toString() {
        return "Requirement : " + this.fieldName + "=" + requiredValue + " on object " + targetObject;
    }
}
