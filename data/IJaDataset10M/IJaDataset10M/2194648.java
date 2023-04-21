package test.example;

import org.jpropeller.bean.impl.BeanDefault;
import org.jpropeller.name.PropName;
import org.jpropeller.properties.EditableProp;
import org.jpropeller.properties.path.impl.PathProp;

/**
 * Demonstrates mirroring of a property, using a {@link PathProp} with a path
 * having only a last name
 * @author shingoki
 */
public class Mirror extends BeanDefault {

    /**
	 * The {@link PropName} for {@link #s()} property
	 */
    public static final PropName<EditableProp<String>, String> S_NAME = PropName.editable("s", String.class);

    private EditableProp<String> s = editable("s", "default s value");

    private EditableProp<String> sMirror = editableFrom("sMirror", String.class).to(S_NAME);

    /**
	 * Access to s
	 * @return
	 * 		s
	 */
    public EditableProp<String> s() {
        return s;
    }

    ;

    /**
	 * Access to sMmirror
	 * @return
	 * 		sMirror property
	 */
    public EditableProp<String> sMirror() {
        return sMirror;
    }

    ;

    @Override
    public String toString() {
        String s = "Mirror";
        return s;
    }
}
