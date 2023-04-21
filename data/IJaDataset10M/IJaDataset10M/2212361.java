package com.codeko.apps.campanilla.ignotus.aracnes.base;

import com.codeko.apps.campanilla.ignotus.util.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.ResourceMap;

public class MagiaTask extends org.jdesktop.application.Task<ResultadoMagia, Void> {

    String isbn = null;

    ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.codeko.apps.campanilla.ignotus.IgnotusApp.class).getContext().getResourceMap(MagiaTask.class);

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public MagiaTask(org.jdesktop.application.Application app, String isbn) {
        super(app);
        setUserCanCancel(true);
        setIsbn(Util.procesarISBN(isbn));
    }

    private String[] getAranas() {
        String[] sA = new String[] { "com.codeko.apps.campanilla.ignotus.aracnes.aranas.MagiaMCU", "com.codeko.apps.campanilla.ignotus.aracnes.aranas.MagiaGoogleBooks", "com.codeko.apps.campanilla.ignotus.aracnes.aranas.MagiaWorldCat" };
        File aranas = new File("aranas.cfg");
        if (aranas.exists()) {
            try {
                Scanner s = new Scanner(aranas);
                Vector<String> ara = new Vector<String>();
                while (s.hasNextLine()) {
                    ara.add(s.nextLine());
                }
                if (ara.size() > 0) {
                    sA = ara.toArray(new String[ara.size()]);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MagiaTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sA;
    }

    public void setMensaje(String mensaje) {
        setMessage(mensaje);
    }

    @Override
    protected ResultadoMagia doInBackground() {
        ResultadoMagia res = new ResultadoMagia(-1, "Error indeterminado", null);
        try {
            setProgress(0.1f);
            if (!Util.esISBNValido(getIsbn())) {
                res = new ResultadoMagia(-2, "ISBN no válido", null);
            } else {
                for (String a : getAranas()) {
                    try {
                        res = new ResultadoMagia(0, "No se ha encontrado el libro");
                        Object o = Class.forName(a).newInstance();
                        if (o instanceof MagiaAbstracta) {
                            MagiaAbstracta ma = (MagiaAbstracta) o;
                            setMessage("Buscando libro en " + ma.getNombre());
                            ma.setIsbn(getIsbn());
                            ma.setMagia(this);
                            ma.setLibroBase(res.getLibro());
                            ma.getLibroBase().setModoSeguro(true);
                            ResultadoMagia rm = ma.iniciar();
                            if (rm.getCodigo() > 0) {
                                res = rm;
                            } else {
                                setMessage("Error: " + rm.getMensaje());
                            }
                        }
                    } catch (Exception e) {
                        Logger.getLogger(MagiaTask.class.getName()).log(Level.WARNING, "No existe la araña", e);
                    }
                }
            }
        } catch (Exception e) {
            res = new ResultadoMagia(-1, "Error indeterminado", null);
            res.setMensajeExtendido(e.getMessage());
        }
        return res;
    }
}
