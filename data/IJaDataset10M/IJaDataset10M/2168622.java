package com.seitenbau.testing.shared.tracer;

import com.seitenbau.testing.shared.model.RecordedTest;

/**
 * Hilfs Interface f�r JUnit Tests bei denen ein Tracer mitl�uft.
 * 
 * Der JUnit Test implementiert diese Interface nicht direkt in Source
 * Code, sondern die Implementierung wir �ber AspectJ in den Test
 * eingebaut.
 */
public interface IUnderTrace {

    /**
   * Liefert den aktuell aufgezeichneten Tracer zur�ck
   * 
   * @return Liefert den aktuell aufgezeichneten Tracer zur�ck
   */
    public RecordedTest getCurrentRecordedTest();
}
