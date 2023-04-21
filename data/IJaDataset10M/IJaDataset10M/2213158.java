package name.huzhenbo.java.algorithm.talent;

/**
 * Represent positions as an array.
 *
 * For example: 23244576  means:
 *
 * 00000000
 * 00000000
 * 10100000
 * 01000000
 * 00011000
 * 00000100
 * 00000001
 * 00000010
 *
 */
class EightQueens {

    public void go() {
        int count = 0;
        for (int i = 0; i < 077777777; i++) {
            int[] positions = toPositions(i);
            if (ok(positions)) {
                print(positions);
                count++;
            }
        }
        System.out.println("Has " + new Integer(count) + " possibilities!");
    }

    private boolean ok(int[] positions) {
        for (int i = 0; i < positions.length; i++) {
            for (int j = i + 1; j < positions.length; j++) {
                if (positions[i] == positions[j] || positions[i] - i == positions[j] - j || positions[i] + i == positions[j] + j) return false;
            }
        }
        return true;
    }

    private void print(int[] positions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < positions.length; i++) {
            sb.append(i + 1);
            sb.append(".");
            sb.append(positions[i] + 1);
            if (i < positions.length - 1) sb.append(" => ");
        }
        System.out.println(sb);
    }

    private int[] toPositions(int n) {
        int[] positions = new int[8];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = n % 8;
            n /= 8;
        }
        return positions;
    }
}
