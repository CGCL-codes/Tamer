package com.core.md;

public class DebitorsMD {

    public static String getPaymentMethod(long paymentMethod) {
        if (paymentMethod == 1) {
            return "Bar";
        } else if (paymentMethod == 2) {
            return "Vorauskasse";
        } else if (paymentMethod == 3) {
            return "Nachnahme";
        } else if (paymentMethod == 4) {
            return "Rechnung";
        } else if (paymentMethod == -1) {
            return "Offerte";
        }
        return "Bar";
    }
}
