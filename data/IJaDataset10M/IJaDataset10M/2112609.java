package ch.trackedbean.copier;

/**
 * Conversion interface for property mappings.<br>
 * You may use it also to compute derived properties.<br>
 * Implementations of this interface must be stateless (and thread safe)!
 * 
 * @author M. Hautle
 * @param <S> The source type
 * @param <D> The destination type
 */
public interface IPropertyConverter<S, D> {

    /**
     * Converts the given source object to the destination type.
     * 
     * @param src The "source" object or null
     * @param dst The current "destination" object or null (you may modify it and return it)
     * @param dstType The desired destination type
     * @param context The mapping context
     * @return The converted object (so a object of the destination type)
     */
    D convertSrc2Dst(S src, D dst, Class<? extends D> dstType, IMappingContext context);

    /**
     * Converts the given destination object to the source type.
     * 
     * @param dst The "destination" object or null
     * @param src The current "source" object or null (you may modify it and return it)
     * @param srcType The desired source type
     * @param context The mapping context
     * @return The converted object (so a object of the source type)
     */
    S convertDst2Src(D dst, S src, Class<? extends S> srcType, IMappingContext context);
}
