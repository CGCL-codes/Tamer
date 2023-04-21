package org.oobench.serialization;

class SerializationPerformanceSimple extends SerializationBenchmark {

    public static void main(String[] args) {
        showLocation();
        testSerialization(50000, SerializeClassSimple.class, "simple serialization", args, 1, "");
    }
}
