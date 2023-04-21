package edu.mit.csail.pag.amock.trace;

import java.io.*;
import java.util.*;

public class ConstructorFixer {

    public static void main(String args[]) throws FileNotFoundException {
        if (args.length != 2) {
            throw new RuntimeException("usage: ConstructorFixer trace-in.xml trace-out.xml");
        }
        String inFileName = args[0];
        String outFileName = args[1];
        Deserializer<TraceEvent> d1 = Deserializer.getDeserializer(new FileInputStream(inFileName), TraceEvent.class);
        Deserializer<TraceEvent> d2 = Deserializer.getDeserializer(new FileInputStream(inFileName), TraceEvent.class);
        Serializer<TraceEvent> s = Serializer.getSerializer(new FileOutputStream(outFileName));
        new ConstructorFixer(d1, d2, s).run();
    }

    private final Deserializer<TraceEvent> firstIn;

    private final Deserializer<TraceEvent> secondIn;

    private final Serializer<TraceEvent> out;

    public ConstructorFixer(Deserializer<TraceEvent> firstIn, Deserializer<TraceEvent> secondIn, Serializer<TraceEvent> out) {
        this.firstIn = firstIn;
        this.secondIn = secondIn;
        this.out = out;
    }

    public void run() {
        final Map<Integer, TraceObject> callIdToInstance = new HashMap<Integer, TraceObject>();
        firstIn.process(new TraceProcessor<TraceEvent>() {

            public void processEvent(TraceEvent ev) {
                if (!(ev instanceof PostCall)) {
                    return;
                }
                PostCall pc = (PostCall) ev;
                if (!pc.isConstructor()) {
                    return;
                }
                assert !callIdToInstance.containsKey(pc.callId);
                callIdToInstance.put(pc.callId, pc.receiver);
            }
        });
        final Set<TraceObject> traceObjectsWithSeenConstructor = new HashSet<TraceObject>();
        secondIn.process(new TraceProcessor<TraceEvent>() {

            public void processEvent(TraceEvent ev) {
                if (ev instanceof PreCall) {
                    PreCall pc = (PreCall) ev;
                    if (pc.isConstructor()) {
                        assert callIdToInstance.containsKey(pc.callId) : String.format("found PreCall for callId %d but never had PostCall", pc.callId);
                        assert pc.receiver instanceof ConstructorReceiver;
                        TraceObject realReceiver = callIdToInstance.get(pc.callId);
                        boolean isTopLevelConstructor = false;
                        if (!traceObjectsWithSeenConstructor.contains(realReceiver) && realReceiver instanceof Instance) {
                            isTopLevelConstructor = true;
                            traceObjectsWithSeenConstructor.add(realReceiver);
                        }
                        ev = pc.copyWithNewReceiverAndTLCFlag(callIdToInstance.get(pc.callId), isTopLevelConstructor);
                    }
                }
                out.write(ev);
            }
        });
        out.close();
    }
}
