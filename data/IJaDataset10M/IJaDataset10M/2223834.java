package stixar.graph.attr;

/**
   Marker Interface for a set of edge attributes stored
   and accessed via arrays of native types.
   <p>
   NativeNodeMaps provide a much more efficient mechanism 
   for storing numeric attributes than via the use of generics.
   The reason for this is that with generics, algorithms with
   numeric requirements, such as shortests paths, end up boxing and 
   unboxing numbers all the time.  Where speed is important and numeric
   processing required, NativeEdgeMaps are the preferred attribute mechanism for 
   numeric attributes. 
   </p>
   Available native attribute types:
   <ul>
   <li>{@link IntNodeMap}</li>
   <li>{@link FloatNodeMap}</li>
   <li>{@link LongNodeMap}</li>
   <li>{@link DoubleNodeMap}</li>
   <li>{@link CharNodeMap}</li>
   <li>{@link ByteNodeMap}</li>
   </ul>
 */
public interface NativeEdgeMap extends NativeMap, EdgeData {
}
