package net.stickycode.mockwire.bdd;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

public class StickyMockitoOngoingStubbingImpl<T> implements StickyMockitoMyOngoingStubbing<T> {

    private final OngoingStubbing<T> mockitoOngoingStubbing;

    public StickyMockitoOngoingStubbingImpl(OngoingStubbing<T> ongoingStubbing) {
        this.mockitoOngoingStubbing = ongoingStubbing;
    }

    public StickyMockitoMyOngoingStubbing<T> willAnswer(Answer<?> answer) {
        return new StickyMockitoOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenAnswer(answer));
    }

    public StickyMockitoMyOngoingStubbing<T> willReturn(T value) {
        return new StickyMockitoOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenReturn(value));
    }

    public StickyMockitoMyOngoingStubbing<T> willReturn(T value, T... values) {
        return new StickyMockitoOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenReturn(value, values));
    }

    public StickyMockitoMyOngoingStubbing<T> willThrow(Throwable... throwables) {
        return new StickyMockitoOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenThrow(throwables));
    }

    public StickyMockitoMyOngoingStubbing<T> willCallRealMethod() {
        return new StickyMockitoOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenCallRealMethod());
    }
}
