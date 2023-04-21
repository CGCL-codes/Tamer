package br.com.caelum.test;

import org.junit.Test;
import br.com.caelum.fixture.Airplane;

public final class RunnerTest {

    @Test
    public void test() {
        new Airplane().fly();
    }
}
