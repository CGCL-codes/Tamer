package edu.cmu.ece.agora.util;

import java.util.Collection;
import java.util.Random;

public final class Tokens {

    private static final Random rand = new Random(System.currentTimeMillis());

    private Tokens() {
        throw new UnsupportedOperationException();
    }

    public static Integer generateToken(Collection<Integer> exclude) {
        Integer token;
        do {
            token = rand.nextInt();
        } while (exclude.contains(token));
        return token;
    }

    public static Long generateToken(Collection<Long> exclude) {
        Long token;
        do {
            token = rand.nextLong();
        } while (exclude.contains(token));
        return token;
    }
}
