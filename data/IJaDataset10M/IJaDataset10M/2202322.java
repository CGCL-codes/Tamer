package com.br.Funcoes;

import android.content.Context;
import android.content.Intent;

public class Funcoes {

    public static void proximaTela(Context context, Class<?> proxima) {
        Intent intent = new Intent(context, proxima);
        context.startActivity(intent);
    }
}
