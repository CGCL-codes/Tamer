package agex.traders;

import agex.db.QuoteTO;
import agex.to.OrderTO;
import agex.to.OrdersTO;

public class MACD extends MultiAssetTrader {

    public void setupTrader() {
        super.setupTrader();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    double preco;

    double last_preco;

    static final int MUITO_ALTO = 0;

    static final int ALTO = 1;

    static final int NEUTRO = 2;

    static final int BAIXO = 3;

    static final int MUITO_BAIXO = 4;

    static int NUM_ESTADOS = 101;

    static int NUM_ACOES = 5;

    static double[] vetor_precos;

    public MACD() {
        super();
    }

    static final int PERIODO = 26;

    static final int SHORT_PERIOD = 12;

    static final int SIGNAL = 9;

    static final int VOLUME = 50000;

    private double dias[][];

    private double signalArray[][];

    public void init() throws Exception {
        dias = new double[papers.length][PERIODO];
        signalArray = new double[papers.length][SIGNAL];
    }

    public OrdersTO doTrade() throws Exception {
        OrdersTO ordens = new OrdersTO();
        for (int i = 0; i < papers.length; i++) {
            ordens.add(doTradeByPaper(i, papers[i]));
        }
        return ordens;
    }

    static boolean lastMacdUnderSignal = true;

    private int currTime = -1;

    public OrderTO doTradeByPaper(int index, String idPaper) throws Exception {
        QuoteTO quote = getQuote(idPaper);
        currTime++;
        double preco = quote.getClose();
        boolean MacdUnderSignal;
        if (currTime < PERIODO) {
            novo_dia(preco, dias[index]);
            return createSingleOrder(idPaper, 0, preco, true);
        } else if (currTime < PERIODO + SIGNAL) {
            double longer = media(dias[index], PERIODO);
            double shorter = media(dias[index], SHORT_PERIOD);
            novo_dia(preco, dias[index]);
            novo_dia(shorter - longer, signalArray[index]);
            return createSingleOrder(idPaper, 0, preco, true);
        } else {
            double longer = media(dias[index], PERIODO);
            double shorter = media(dias[index], SHORT_PERIOD);
            double signal = media(signalArray[index], SIGNAL);
            if ((shorter - longer) > signal) MacdUnderSignal = false; else MacdUnderSignal = true;
            int v = 0;
            if (!MacdUnderSignal && lastMacdUnderSignal) {
                v = VOLUME;
            } else if (MacdUnderSignal && !lastMacdUnderSignal) {
                v = -1 * VOLUME;
            } else v = 0;
            lastMacdUnderSignal = MacdUnderSignal;
            novo_dia(preco, dias[index]);
            novo_dia(shorter - longer, signalArray[index]);
            return createSingleOrder(idPaper, Math.abs(v), preco, v > 0 ? true : false);
        }
    }

    public static double media(double dias[], int ndias) {
        double t = 0;
        for (int i = dias.length - 1; i >= (dias.length - ndias); i--) t = t + dias[i];
        return (t / ndias);
    }

    public static void novo_dia(double p, double dias[]) {
        int tam = dias.length;
        for (int i = 0; i < tam - 1; i++) dias[i] = dias[i + 1];
        dias[tam - 1] = p;
    }

    public static double media(double d[]) {
        return media(d, d.length);
    }
}
