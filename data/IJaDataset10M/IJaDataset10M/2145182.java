package xxl.core.io.converters;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Time;

/**
 * A Converter for object of type {@link java.sql.Time}.
 */
public class TimeConverter extends FixedSizeConverter<Time> {

    /**
	 * This instance can be used for getting a default instance of a time
	 * converter. It is similar to the <i>Singleton Design Pattern</i> (for
	 * further details see Creational Patterns, Prototype in <i>Design
	 * Patterns: Elements of Reusable Object-Oriented Software</i> by Erich
	 * Gamma, Richard Helm, Ralph Johnson, and John Vlissides) except that
	 * there are no mechanisms to avoid the creation of other instances of a
	 * time converter.
	 */
    public static final TimeConverter DEFAULT_INSTANCE = new TimeConverter();

    /**
	 * This field contains the number of bytes needed to serialize the
	 * <code>long</code> value of a <code>Time</code> object. Because this size
	 * is predefined it must not be measured each time.
	 */
    public static final int SIZE = 8;

    /**
	 * Sole constructor. (For invocation by subclass constructors, typically
	 * implicit.)
	 */
    public TimeConverter() {
        super(SIZE);
    }

    /** 
	 * Reads the <code>long</code> value for the specified (<code>Time</code>)
	 * object from the specified data input and returns the restored object.
	 * 
	 * @param dataInput the stream to read the <code>long</code> value from in
	 *        order to return an <code>Time</code> object.
	 * @param object the (<code>Time</code>) object to be restored.
	 * @return the read <code>Time</code> object.
	 * @throws IOException if I/O errors occur.
	 */
    @Override
    public Time read(DataInput dataInput, Time object) throws IOException {
        long time = LongConverter.DEFAULT_INSTANCE.readLong(dataInput);
        if (object == null) object = new Time(time); else object.setTime(time);
        return object;
    }

    /**
	 * Writes the <code>long</code> value of the specified <code>Time</code>
	 * object to the specified data output.  
	 *
	 * @param dataOutput the stream to write the <code>long</code> value of the
	 *        specified <code>Time</code> object to.
	 * @param object the <code>Time</code> object that <code>long</code> value
	 *        should be written to the data output.
	 * @throws IOException includes any I/O exceptions that may occur.
	 */
    @Override
    public void write(DataOutput dataOutput, Time object) throws IOException {
        LongConverter.DEFAULT_INSTANCE.writeLong(dataOutput, object.getTime());
    }
}
