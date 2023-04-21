package modelo;

public class Transformacao {

    private double[][] matriz;

    private Ponto referencia;

    public Transformacao() {
        matriz = new double[3][3];
    }

    public Transformacao(int xReferencia, int yReferencia) {
        matriz = new double[3][3];
        referencia = new Ponto(xReferencia, yReferencia);
    }

    public void setMatrizTranslacao(int H, int V) {
        matriz[0][0] = 1;
        matriz[0][1] = 0;
        matriz[0][2] = H;
        matriz[1][0] = 0;
        matriz[1][1] = 1;
        matriz[1][2] = V;
        matriz[2][0] = 0;
        matriz[2][1] = 0;
        matriz[2][2] = 1;
    }

    public void setMatrizEscala(double escalaX, double escalaY) {
        matriz[0][0] = escalaX;
        matriz[0][1] = 0;
        matriz[0][2] = 0;
        matriz[1][0] = 0;
        matriz[1][1] = escalaY;
        matriz[1][2] = 0;
        matriz[2][0] = 0;
        matriz[2][1] = 0;
        matriz[2][2] = 1;
    }

    public void setMatrizRotacao(double angulo) {
        double radianos = Math.toRadians(angulo);
        matriz[0][0] = Math.cos(radianos);
        matriz[0][1] = -Math.sin(radianos);
        matriz[0][2] = 0;
        matriz[1][0] = Math.sin(radianos);
        matriz[1][1] = Math.cos(radianos);
        matriz[1][2] = 0;
        matriz[2][0] = 0;
        matriz[2][1] = 0;
        matriz[2][2] = 1;
    }

    /**
	 * M�todo respons�vel pela transforma��o de um ponto da figura
	 * @param trans
	 * @param x
	 * @param y	
	 * @return retorna o ponto transformado
	 */
    public static Ponto transformaPonto(Transformacao trans, int x, int y) {
        double xDeslocado = x - trans.referencia.getX();
        double yDeslocado = y - trans.referencia.getY();
        Ponto p = produtoResultado(trans, xDeslocado, yDeslocado);
        x = p.getX() + trans.referencia.getX();
        y = p.getY() + trans.referencia.getY();
        p.setX(x);
        p.setY(y);
        return p;
    }

    /**
	 * M�todo que realiza a multiplica��o da matriz de transforma��o (3 x 3) e a matriz que representa
	 * um ponto da figura (1 x 3)
	 * @param trans
	 * @param x
	 * @param y
	 * @return
	 */
    public static Ponto produtoResultado(Transformacao trans, double x, double y) {
        double matriz[][] = { { x }, { y }, { 1 } };
        double aux;
        double resultado[][] = new double[3][1];
        for (int i = 0; i < 3; i++) {
            aux = 0;
            for (int j = 0; j < 3; j++) {
                aux += (matriz[j][0] * trans.matriz[i][j]);
            }
            resultado[i][0] = aux;
        }
        return new Ponto((int) Math.round(resultado[0][0]), (int) Math.round(resultado[1][0]));
    }

    /**
	 * M�todo que realiza a multiplica��o de duas matriz de transforma��o (utilizado para transforma��es compostas)
	 * @param transformacao
	 * @param transformacao2
	 * @return
	 */
    public static Transformacao produtoDeMatriz(Transformacao transformacao, Transformacao transformacao2) {
        double aux;
        Transformacao resultado = new Transformacao();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                aux = 0;
                for (int k = 0; k < 3; k++) {
                    aux += (transformacao.matriz[i][k] * transformacao2.matriz[k][j]);
                }
                resultado.matriz[i][j] = aux;
            }
        }
        return resultado;
    }

    public double[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(double[][] matriz) {
        this.matriz = matriz;
    }

    public Ponto getReferencia() {
        return referencia;
    }

    public void setReferencia(Ponto referencia) {
        this.referencia = referencia;
    }
}
