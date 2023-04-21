package maiabee.mtai.numbers.fibonacci;

class IterativeFibonacciSeries implements FibonacciSeries {

    public int getFibonacci(int n) {
        int prev[] = { 1, 1 };
        for (int i = 1; i < n; i++) {
            int aux = prev[1] + prev[0];
            prev[0] = prev[1];
            prev[1] = aux;
        }
        return prev[1];
    }
}
