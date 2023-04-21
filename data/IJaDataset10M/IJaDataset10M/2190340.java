package ao.ai.evo.coding.invoke;

import ao.ai.evo.product.Product;
import ao.ai.evo.coding.Coding;
import ao.ai.evo.coding.GpCoding;

/**
 *
 */
public abstract class AbstractMessage implements Message {

    private final boolean[] productArgs;

    @SuppressWarnings("unchecked")
    public AbstractMessage(Class params[]) {
        productArgs = new boolean[params.length];
        for (int i = 0; i < productArgs.length; i++) {
            productArgs[i] = params[i].isAssignableFrom(Product.class);
        }
    }

    public Coding asCoding() {
        return new GpCoding(isStatic(), new GpDispatcher(this));
    }

    public boolean[] productArgs() {
        return productArgs;
    }

    public abstract Object dispatch(Object receiver, Object... args);
}
