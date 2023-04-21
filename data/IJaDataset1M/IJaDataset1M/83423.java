package Datos;

import Geo.*;
import Interfeis.*;

public class cargar extends datos {

    public cargar() {
    }

    public void Tiler(Tiler destino, int Ref_Til) {
        int i = 0;
        int j = 0;
        destino.Tiles = new int[3];
        destino.TipoT = 0;
        destino.Ancho_Map = 101;
        destino.Largo_Map = 101;
        destino.Mapa_TIL = new int[(destino.Ancho_Map * destino.Largo_Map)];
        destino.Ancho_Til = 32;
        destino.Largo_Til = 32;
        destino.New_X = 0;
        destino.New_Y = 0;
        destino.Til_X = 0;
        destino.Til_Y = 0;
        destino.Des_X = 0;
        destino.Des_Y = 0;
        destino.Gru_Til = 0;
        for (i = 0; i <= ((destino.Ancho_Map * destino.Largo_Map) - 1); i++) {
            destino.Mapa_TIL[i] = 0;
            if ((i % (destino.Largo_Map + 1)) == 0) destino.Mapa_TIL[i] = 1;
            if ((i % (destino.Largo_Map)) == 0) destino.Mapa_TIL[i] = 1;
            if ((i % (destino.Largo_Map - 1)) == 0) destino.Mapa_TIL[i] = 1;
            if (i < (destino.Largo_Map - 1)) destino.Mapa_TIL[i] = 1;
        }
    }

    public int[] Map_TIL(int Gru_Til, graf Mgraf) {
        return Map_array(0, Mgraf);
    }

    public int[] Map_array(int index, graf Mgraf) {
        if (index == 0) return Mgraf.Ini_Array_Ima("sprites/tile3.bmp");
        if (index == 1) return Mgraf.Ini_Array_Ima("sprites/direccion.bmp");
        if (index == 2) return Mgraf.Ini_Array_Ima("sprites/tia.bmp");
        return Mgraf.Ini_Array_Ima("sprites/direccion.bmp");
    }

    public void Geos(geo destino, int zona) {
        int i = 0;
        int j = 0;
        destino.ZONA = zona;
        destino.Ancho = 101;
        destino.Largo = 101;
        destino.Ref_x = 0;
        destino.Ref_y = 0;
        destino.Ref_Ancho = 800;
        destino.Ref_Largo = 600;
        destino.ISO_X = 0;
        destino.ISO_Y = 0;
        destino.ISO_Z = 0;
        destino.REF_Til_Terremo = new int[2];
        destino.REF_Til_Terremo[0] = 1;
        destino.REF_Til_Terremo[1] = 0;
        destino.Til_TER = new Tiler[2];
        destino.Mapa_ISO = new baldosa_ISO[(destino.Ancho * destino.Largo)];
        for (i = 0; i <= ((destino.Ancho * destino.Largo) - 1); i++) {
            destino.Mapa_ISO[i] = new baldosa_ISO();
        }
    }
}
