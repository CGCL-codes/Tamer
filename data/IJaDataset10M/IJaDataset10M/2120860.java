package com.novatialabs.qttt.gwt.client;

import junit.framework.TestCase;

public abstract class NotationTestCase extends TestCase {

    private Notation notation;

    public NotationTestCase() {
        super();
    }

    protected abstract Notation createNotation();

    public NotationTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        notation = createNotation();
    }

    protected void checkParseMove(NotationSample notationSample) {
        String canonicleSample = notationSample.getCanonicleSample();
        for (String sample : notationSample.getSamples()) {
            Move move = notation.parseMove(sample);
            String result = notation.printMove(move);
            assertEquals("printMove(parseMove(\"" + sample + "\"))", canonicleSample, result);
        }
    }

    protected void checkParseGame(NotationSample notationSample) {
        String canonicleSample = notationSample.getCanonicleSample();
        for (String sample : notationSample.getSamples()) {
            GameModel game = notation.parseGame(sample);
            String result = notation.printGame(game);
            assertEquals("printMove(parseMove(\"" + sample + "\"))", canonicleSample, result);
        }
    }
}
