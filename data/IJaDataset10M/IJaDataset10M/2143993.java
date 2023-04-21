package com.chatcliente.privados.envio;

import java.util.Calendar;

/**
 *
 * @author cracker
 */
public class Velocidad implements Runnable {

    private EnvioArchivos _frame;

    private Calendar timer;

    private Envia _oido;

    public Velocidad(EnvioArchivos _frame, Envia _oido) {
        this._frame = _frame;
        this._oido = _oido;
    }

    public void run() {
        timer = Calendar.getInstance();
        int seg = 0;
        _oido.cont = 0;
        seg = timer.get(Calendar.SECOND);
        while (_oido.continuar) {
            timer = Calendar.getInstance();
            if (timer.get(Calendar.SECOND) != seg) {
                _frame.lblVelo.setText("" + (_oido.cont / 1024));
                _oido.cont = 0;
                seg = timer.get(Calendar.SECOND);
                try {
                    _frame.lblTime.setText("" + (((_frame.barra.getMaximum() - _frame.barra.getValue()) / 1024) / Integer.parseInt(_frame.lblVelo.getText())));
                } catch (java.lang.ArithmeticException e) {
                }
            }
        }
        _frame.lblVelo.setText("0");
        _frame.lblTime.setText("-");
    }
}
