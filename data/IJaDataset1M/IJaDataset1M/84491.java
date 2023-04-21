package ao.ai.evo.coding;

import ao.ai.evo.coding.invoke.GpDispatcher;
import ao.ai.evo.product.Product;
import ao.ai.evo.gene.Gene;

/**
 *
 */
public class GpCoding implements Coding {

    private boolean firstArgAsInstance;

    private GpDispatcher dispatcher;

    public GpCoding(boolean isStatic, GpDispatcher messageDispatcher) {
        firstArgAsInstance = !isStatic;
        dispatcher = messageDispatcher;
    }

    public Product instance(Gene... dependencies) {
        return firstArgAsInstance ? dependencies[0].express() : null;
    }

    public Product[] args(Gene... dependencies) {
        int offset = firstArgAsInstance ? 1 : 0;
        Product args[] = new Product[dependencies.length - offset];
        for (int i = 0; i < args.length; i++) {
            args[i] = dependencies[i + offset].express();
        }
        return args;
    }

    public Product encode(Gene... dependencies) {
        return dispatcher.dispatch(instance(dependencies), args(dependencies));
    }

    @Override
    public String toString() {
        return dispatcher.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof GpCoding)) return false;
        GpCoding gpCoding = (GpCoding) o;
        return firstArgAsInstance == gpCoding.firstArgAsInstance && dispatcher.equals(gpCoding.dispatcher);
    }

    @Override
    public int hashCode() {
        int result;
        result = (firstArgAsInstance ? 1 : 0);
        result = 31 * result + dispatcher.hashCode();
        return result;
    }
}
