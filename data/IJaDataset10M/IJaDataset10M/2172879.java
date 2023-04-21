package drjava.smyle.core;

import java.util.*;
import drjava.smyle.meta.*;

interface TableIterator<T extends Struct<T>> extends Iterator<T> {

    ChunkRef nextChunk();
}
