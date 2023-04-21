package jp.seraph.jsade.perceptor;

public class TimePerceptor implements Perceptor {

    public TimePerceptor(double aValue) {
        mValue = aValue;
    }

    private double mValue;

    /**
     *
     * @see jp.seraph.jsade.perceptor.Perceptor#accept(jp.seraph.jsade.perceptor.PerceptorVisitor)
     */
    public void accept(PerceptorVisitor aVisitor) {
        aVisitor.visit(this);
    }

    /**
     * サーバの起動時点からの経過秒数を取得する
     * TODO 今後doubleでなく、TimeSpanを表すクラスのインスタンスにするかも
     * @return
     */
    public double getValue() {
        return mValue;
    }
}
