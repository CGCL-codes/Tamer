package wordtutor.util;

import java.io.Serializable;

/**
 * describes my own serializable interface 
 *
 */
public interface IWTSerializable extends Serializable {

    void saveToXML();

    void loadFromXML();

    /**
	 * @deprecated
	 */
    void generateDefault();
}
