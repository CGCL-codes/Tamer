package org.bing.adapter.com.caucho.hessian.io;

import java.util.logging.*;
import java.io.*;
import org.bing.adapter.com.caucho.hessian.util.HessianFreeList;

/**
 * Factory for creating HessianInput and HessianOutput streams.
 */
public class HessianFactory {

    public static final Logger log = Logger.getLogger(HessianFactory.class.getName());

    private SerializerFactory _serializerFactory;

    private SerializerFactory _defaultSerializerFactory;

    private final HessianFreeList<Hessian2Output> _freeHessian2Output = new HessianFreeList<Hessian2Output>(32);

    private final HessianFreeList<HessianOutput> _freeHessianOutput = new HessianFreeList<HessianOutput>(32);

    private final HessianFreeList<Hessian2Input> _freeHessian2Input = new HessianFreeList<Hessian2Input>(32);

    private final HessianFreeList<HessianInput> _freeHessianInput = new HessianFreeList<HessianInput>(32);

    public HessianFactory() {
        _defaultSerializerFactory = SerializerFactory.createDefault();
        _serializerFactory = _defaultSerializerFactory;
    }

    public void setSerializerFactory(SerializerFactory factory) {
        _serializerFactory = factory;
    }

    public SerializerFactory getSerializerFactory() {
        if (_serializerFactory == _defaultSerializerFactory) {
            _serializerFactory = new SerializerFactory();
        }
        return _serializerFactory;
    }

    /**
   * Creates a new Hessian 2.0 deserializer.
   */
    public Hessian2Input createHessian2Input(InputStream is) {
        Hessian2Input in = new Hessian2Input(is);
        in.setSerializerFactory(_serializerFactory);
        return in;
    }

    /**
   * Frees a Hessian 2.0 deserializer
   */
    public void freeHessian2Input(Hessian2Input in) {
    }

    /**
   * Creates a new Hessian 2.0 deserializer.
   */
    public Hessian2StreamingInput createHessian2StreamingInput(InputStream is) {
        Hessian2StreamingInput in = new Hessian2StreamingInput(is);
        in.setSerializerFactory(_serializerFactory);
        return in;
    }

    /**
   * Frees a Hessian 2.0 deserializer
   */
    public void freeHessian2StreamingInput(Hessian2StreamingInput in) {
    }

    /**
   * Creates a new Hessian 1.0 deserializer.
   */
    public HessianInput createHessianInput(InputStream is) {
        return new HessianInput(is);
    }

    /**
   * Creates a new Hessian 2.0 serializer.
   */
    public Hessian2Output createHessian2Output(OutputStream os) {
        Hessian2Output out = _freeHessian2Output.allocate();
        if (out != null) out.init(os); else out = new Hessian2Output(os);
        out.setSerializerFactory(_serializerFactory);
        return out;
    }

    /**
   * Frees a Hessian 2.0 serializer
   */
    public void freeHessian2Output(Hessian2Output out) {
        if (out == null) return;
        out.free();
        _freeHessian2Output.free(out);
    }

    /**
   * Creates a new Hessian 2.0 serializer.
   */
    public Hessian2StreamingOutput createHessian2StreamingOutput(OutputStream os) {
        Hessian2Output out = createHessian2Output(os);
        return new Hessian2StreamingOutput(out);
    }

    /**
   * Frees a Hessian 2.0 serializer
   */
    public void freeHessian2StreamingOutput(Hessian2StreamingOutput out) {
        if (out == null) return;
        freeHessian2Output(out.getHessian2Output());
    }

    /**
   * Creates a new Hessian 1.0 serializer.
   */
    public HessianOutput createHessianOutput(OutputStream os) {
        return new HessianOutput(os);
    }

    public OutputStream createHessian2DebugOutput(OutputStream os, Logger log, Level level) {
        HessianDebugOutputStream out = new HessianDebugOutputStream(os, log, level);
        out.startTop2();
        return out;
    }
}
