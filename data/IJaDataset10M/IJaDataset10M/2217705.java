package com.liferay.util;

import java.io.Serializable;

/**
 * <a href="Converter.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public interface Converter extends Serializable {

    public Object convert(String s) throws ConverterException;
}
