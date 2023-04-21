package taskgraph.tasks.data;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import taskgraph.ports.InputPort;
import taskgraph.ports.OutputPort;
import taskgraph.tasks.PrimitiveTask;

/**
 * Makes duplicates of objects coming from the input port.
 * 
 * <p>This task will output two objects for every one coming from the input
 * port. The input object will be transported through output port 'original'
 * and the copy through output port 'replica'.
 * 
 * <p>Elements can be copied by implementing Copy Constructor or Clone interface.
 * A copy constructor is simply a constructor that takes as a single argument
 * an object of the same class, which is than used to initialize all of the
 * new object's state:
 * <pre>
 * class MyClass {
 *     private int key;
 *     private String name;
 *     public MyClass(int k, String n) { key = k; name = n; }
 *     public MyClass(MyClass my) { key = my.key; name = my.name; }
 *     ...
 * }
 * </pre> 
 * @author Armando Blancas
 * @see taskgraph.ports.InputPort
 * @see taskgraph.ports.OutputPort
 * @param <E> The type of elements replicated by this task.
 */
public class Replicate<E> extends PrimitiveTask {

    private InputPort<E> input;

    private OutputPort<E> original;

    private OutputPort<E> replica;

    /**
	 * This constructor allows creating instances as beans.
	 */
    public Replicate() {
    }

    /**
     * Creates a configured instance.
     * 
     * @param input The input port.
     * @param original The output port passing on the input elements.
     * @param replica The output port transporting copies of input elements.
     */
    public Replicate(final InputPort<E> input, final OutputPort<E> original, final OutputPort<E> replica) {
        setInput(input);
        setOriginal(original);
        setReplica(replica);
    }

    /**
     * Gets the input port.
     * 
	 * @return The element input port.
	 */
    public InputPort<E> getInput() {
        return input;
    }

    /**
     * Gets the output port for elements from the input port.
     * 
	 * @return The output port.
	 */
    public OutputPort<E> getOriginal() {
        return original;
    }

    /**
     * Gets the output port for copies of elements from the input port.
     * 
	 * @return the replica
	 */
    public OutputPort<E> getReplica() {
        return replica;
    }

    /**
     * Performs the work of this task.
     * 
	 *<pre>
	 *while not EOF
	 *    read an object reference from input port
	 *    if object class has copy constructor
	 *        loop through input
	 *            write original object
	 *            write copied object
	 *    if object class has a clone method
	 *        loop through input
	 *            write original object
	 *            write copied object
	 *    if object class has neither copy constructor
	 *    or clone method, log the error and quit.
	 *close input port
	 *close port 'original'
	 *close port 'replica'
	 *</pre>
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            E element = input.read();
            Class<?> clazz = element.getClass();
            Constructor constructor = clazz.getDeclaredConstructor(clazz);
            if (constructor != null) {
                while (!Thread.currentThread().isInterrupted()) {
                    original.write(element);
                    replica.write((E) constructor.newInstance(element));
                    element = input.read();
                }
            } else {
                Method clone = clazz.getDeclaredMethod("clone");
                while (!Thread.currentThread().isInterrupted()) {
                    original.write(element);
                    replica.write((E) clone.invoke(element));
                    element = input.read();
                }
            }
        } catch (InterruptedIOException ie) {
        } catch (InstantiationException e) {
            log.error("Replicate#run()", e);
        } catch (ClassCastException e) {
            log.error("Replicate#run()", e);
        } catch (SecurityException e) {
            log.error("Replicate#run()", e);
        } catch (NoSuchMethodException e) {
            log.warn("Replicate#run(): No copy constructor or clone");
        } catch (IllegalArgumentException e) {
            log.error("Replicate#run()", e);
        } catch (IllegalAccessException e) {
            log.error("Replicate#run()", e);
        } catch (InvocationTargetException e) {
            log.error("Replicate#run()", e);
        } catch (EOFException e) {
        } catch (IOException e) {
            log.error("Replicate#run()", e);
        } finally {
            input.close();
            original.close();
            replica.close();
        }
    }

    /**
	 * Sets the element input port.
	 * 
	 * @param input The input port.
	 */
    public void setInput(final InputPort<E> input) {
        if (input == null) throw new IllegalArgumentException("input == null");
        this.input = input;
    }

    /**
	 * Sets the output port for elements from the input port.
	 * 
	 * @param original The output port.
	 */
    public void setOriginal(final OutputPort<E> original) {
        if (original == null) throw new IllegalArgumentException("original == null");
        this.original = original;
    }

    /**
	 * Sets the output port for copies of elements from the input port.
	 * 
	 * @param replica An output port to set.
	 */
    public void setReplica(OutputPort<E> replica) {
        if (replica == null) throw new IllegalArgumentException("replica == null");
        this.replica = replica;
    }
}
