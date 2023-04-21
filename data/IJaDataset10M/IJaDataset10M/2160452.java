package com.bimbodroid.environnement;

import com.bimbodroid.entity.Bimbo;
import com.bimbodroid.R;

/**
 * <string>Club</strong> environnement class
 * @author Romain Lopez
 */
public class Club extends Environement {

    /**
	 * default constructor
	 */
    public Club() {
        this.background = R.drawable.club;
        this.ActionList = new String[4];
        this.ActionList[0] = "Eat";
        this.ActionList[1] = "Talk";
        this.ActionList[2] = "Dance";
        this.ActionList[3] = "Work";
    }

    /**
	 * Action <strong>Eat</strong> : increase Hunger value
	 * @param b the bimbo reference
	 * @param o null
	 */
    @Override
    public void Eat(Bimbo b, Object o) {
        b.Set_Hunger(b.Get_Hunger() + 20);
        b.Set_Money(b.Get_Money() - 10);
        b.Set_Moral(b.Get_Moral() + 5);
    }

    /**
	 * Action <strong>Talk</strong> : increase Moral
	 * @param b the bimbo reference
	 * @param o null
	 */
    @Override
    public void Talk(Bimbo b, Object o) {
        b.Set_Moral(b.Get_Moral() + 5);
    }

    /**
	 * Action <strong>Dance</strong> : increase Moral
	 * @param b the bimbo reference
	 * @param o null
	 */
    public void Dance(Bimbo b) {
        b.Set_Money(b.Get_Money() - 20);
        b.Set_Hygiene(b.Get_Hygiene() - 20);
        b.Set_Moral(b.Get_Moral() + 10);
        b.Set_Tireness(b.Get_Tireness() - 20);
    }

    /**
	 * Action <strong>Work</strong> : to earn money
	 * @param b the bimbo reference
	 * @param o null
	 */
    public void Work(Bimbo b) {
        b.Set_Money(b.Get_Money() + 40);
        b.Set_Hygiene(b.Get_Hygiene() - 20);
        b.Set_Tireness(b.Get_Tireness() - 50);
    }
}
