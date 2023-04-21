package taskgraph.tasks.data;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import taskgraph.ports.InputPort;
import taskgraph.ports.OutputPort;
import taskgraph.tasks.PrimitiveTask;

/**
 * Assigns property values to objects of type E.
 * 
 * <p>Input channels transport {@code Object} references. Target elements for 
 * assignment come from an input port and are put through the output port.
 * 
 * @author Armando Blancas
 * @see taskgraph.ports.InputPort
 * @see taskgraph.ports.OutputPort
 * @param <E> The type of elements that receive property values.
 */
public class Assign<E> extends PrimitiveTask {

    private InputPort<E> input;

    private OutputPort<E> output;

    private List<InputPort<Object>> inputPorts;

    private List<String> propertyNames;

    /**
	 * This constructor allows creating instances as beans.
	 */
    public Assign() {
        inputPorts = new LinkedList<InputPort<Object>>();
        propertyNames = new LinkedList<String>();
    }

    /**
     * Assign constructor.
     * 
     * @param input The input port.
     * @param output The output port.
     */
    public Assign(final InputPort<E> input, final OutputPort<E> output) {
        this();
        setInput(input);
        setOutput(output);
    }

    /**
     * Gets the input port.
     * 
	 * @return The input port.
	 */
    public InputPort<E> getInput() {
        return input;
    }

    /**
     * Gets the input port at {@code index}.
     *
     * @param index The index of the value to retrieve.
	 * @return The input port.
	 */
    public InputPort<Object> getInput(int index) {
        return inputPorts.get(index);
    }

    /**
     * Gets the output port.
     * 
	 * @return The output port.
	 */
    public OutputPort<E> getOutput() {
        return output;
    }

    /**
	 * Gets the name of the Assign property at {@code index}. 
	 * 
     * @param index The index of the value to retrieve.
	 * @return The property {@code String} value.
	 */
    public String getPropertyName(int index) {
        return propertyNames.get(index);
    }

    /**
     * Performs the work of this task.
     * 
     * This task expects a list of property names and a list
     * of corresponding input ports for the properties.
	 *<pre>
	 *while not EOF
	 *    read element E from input port
	 *    for each indexed input port
	 *        read an object reference as a value
	 *        use setter to apply the value
	 *    write element E to the output port
	 *close input ports
	 *close output port
	 *</pre>
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        InputPort[] ports = null;
        try {
            E instance = input.read();
            List<Method> methodList = new LinkedList<Method>();
            BeanInfo info = Introspector.getBeanInfo(instance.getClass());
            for (String s : propertyNames) {
                for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                    if (pd.getName().equals(s)) {
                        methodList.add(pd.getWriteMethod());
                    }
                }
            }
            Method[] setters = methodList.toArray(new Method[0]);
            ports = inputPorts.toArray(new InputPort[0]);
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 0; i < setters.length; ++i) {
                    setters[i].invoke(instance, ports[i].read());
                }
                output.write(instance);
                instance = input.read();
            }
        } catch (InterruptedIOException e) {
        } catch (IntrospectionException e) {
            log.error("Assign#run()", e);
        } catch (IllegalArgumentException e) {
            log.error("Assign#run()", e);
        } catch (IllegalAccessException e) {
            log.error("Assign#run()", e);
        } catch (InvocationTargetException e) {
            log.error("Assign#run()", e);
        } catch (EOFException e) {
        } catch (IOException e) {
            log.error("Assign#run()", e);
        } finally {
            input.close();
            output.close();
            if (ports != null) {
                for (InputPort port : ports) port.close();
            }
        }
    }

    /**
	 * Sets the input port.
	 * 
	 * @param input The input port.
	 */
    public void setInput(final InputPort<E> input) {
        if (input == null) throw new IllegalArgumentException("input == null");
        this.input = input;
    }

    /**
	 * Sets the input port at {@code index}.
	 * 
     * @param index The index of the value to set.
	 * @param input The input port.
	 */
    public void setInput(final int index, final InputPort<Object> input) {
        if (input == null) throw new IllegalArgumentException("input == null");
        inputPorts.add(index, input);
    }

    /**
	 * Sets the output port.
	 * 
	 * @param output The output port.
	 */
    public void setOutput(final OutputPort<E> output) {
        if (output == null) throw new IllegalArgumentException("output == null");
        this.output = output;
    }

    /**
	 * Sets the name of the property to be Assigned, at {@code index}.
	 * 
     * @param index The index of the value to set.
	 * @param propertyName The {@code String} value to set.
	 */
    public void setPropertyName(final int index, final String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName == null");
        }
        propertyNames.add(index, propertyName);
    }
}
