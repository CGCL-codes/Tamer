package Romain_Lopez;

import Stephane_Bernard.Bimbo;

public class Barber extends Environement {

    public Barber() {
    }

    void Cut_Hair(Bimbo b, Hair_Cut cut) {
        b.Set_Moral(b.Get_Moral() + 10);
        b.Get_Apparence.Set_haircut(cut);
    }

    void Hair_Dye(Bimbo b, Hair_Color color) {
        b.Set_Moral(b.Get_Moral() + 10);
        b.Get_Apparence.Set_haircol(color);
    }
}
