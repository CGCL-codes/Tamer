package com.gwtapps.messenger.client.view;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtapps.messenger.client.model.Contact;

public class Character {

    AbsolutePanel map;

    Contact contact;

    Image characterImage;

    Label nameLbl;

    MessengerView view;

    Timer charTimer;

    String situation = "NOOP";

    int charOngridX = 0;

    int charOngridY = 0;

    public Character(final Contact contact, final AbsolutePanel map, MessengerView view) {
        this.contact = contact;
        this.map = map;
        this.view = view;
        characterImage = new Image("./gfx/critter/art/critters/HMMAXXAA/HMMAXXAA.gif-" + contact.direction + "-00.gif");
        characterImage.setStyleName("dukee");
        characterImage.setTitle(contact.getName());
        map.add(characterImage);
        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
        nameLbl = new Label(contact.getName());
        nameLbl.setStyleName("outerAbsolutePanel");
        nameLbl.setTitle(contact.getName());
        map.add(nameLbl);
        map.setWidgetPosition(nameLbl, contact.charPosOnMapX, contact.charPosOnMapY - 10);
        charTimer = new Timer() {

            @Override
            public void run() {
                DOM.setStyleAttribute(characterImage.getElement(), "zIndex", "" + charOngridY);
                map.setWidgetPosition(nameLbl, contact.charPosOnMapX, contact.charPosOnMapY - 10);
                int ClickPositionForCharX = ((contact.gridx * 32) - (contact.gridy * 16));
                int ClickPositionForCharY = (contact.gridy * 12) - 52;
                if (ClickPositionForCharX > contact.charPosOnMapX && ClickPositionForCharY > contact.charPosOnMapY) {
                    contact.direction = 2;
                    contact.charPosOnMapX = contact.charPosOnMapX + 4;
                    contact.charPosOnMapY = contact.charPosOnMapY + 3;
                    int[] gridPoint = MapView.SetGridPosition(contact.charPosOnMapX, contact.charPosOnMapY + 52);
                    charOngridX = gridPoint[0];
                    charOngridY = gridPoint[1];
                    if ((contact.charPosOnMapX / 4) % 8 == 2) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-0.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 3) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-1.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 4) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-2.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 5) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-3.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 6) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-4.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 7) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-5.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 2, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 0) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-6.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 1) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-2-7.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 1, contact.charPosOnMapY);
                    }
                } else if (ClickPositionForCharX < contact.charPosOnMapX && ClickPositionForCharY < contact.charPosOnMapY) {
                    contact.direction = 5;
                    contact.charPosOnMapX = contact.charPosOnMapX - 4;
                    contact.charPosOnMapY = contact.charPosOnMapY - 3;
                    int[] gridPoint = MapView.SetGridPosition(contact.charPosOnMapX, contact.charPosOnMapY + 52);
                    charOngridX = gridPoint[0];
                    charOngridY = gridPoint[1];
                    if ((contact.charPosOnMapX / 4) % 8 == 2) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-7.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 2, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 3) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-6.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 4) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-5.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 5) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-4.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 6) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-3.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 7) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-2.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 0) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-1.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 1) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-5-0.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    }
                } else if (ClickPositionForCharX < contact.charPosOnMapX && ClickPositionForCharY > contact.charPosOnMapY) {
                    contact.direction = 3;
                    contact.charPosOnMapX = contact.charPosOnMapX - 4;
                    contact.charPosOnMapY = contact.charPosOnMapY + 3;
                    int[] gridPoint = MapView.SetGridPosition(contact.charPosOnMapX, contact.charPosOnMapY + 52);
                    charOngridX = gridPoint[0];
                    charOngridY = gridPoint[1];
                    if ((contact.charPosOnMapX / 4) % 8 == 2) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-0.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 3) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-1.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 4) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-2.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 5) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-3.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 6) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-4.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY + 1);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 7) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-5.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 0) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-6.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY - 1);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 1) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-3-7.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 2, contact.charPosOnMapY);
                    }
                } else if (ClickPositionForCharX > contact.charPosOnMapX && ClickPositionForCharY < contact.charPosOnMapY) {
                    contact.direction = 0;
                    contact.charPosOnMapX = contact.charPosOnMapX + 4;
                    contact.charPosOnMapY = contact.charPosOnMapY - 3;
                    int[] gridPoint = MapView.SetGridPosition(contact.charPosOnMapX, contact.charPosOnMapY + 52);
                    charOngridX = gridPoint[0];
                    charOngridY = gridPoint[1];
                    if ((contact.charPosOnMapX / 4) % 8 == 2) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-7.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY - 1);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 3) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-6.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY - 2);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 4) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-5.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY - 1);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 5) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-4.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 6) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-3.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 7) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-2.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 0) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-1.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 4) % 8 == 1) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-0-0.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    }
                } else if (ClickPositionForCharX > contact.charPosOnMapX) {
                    contact.direction = 1;
                    contact.charPosOnMapX = contact.charPosOnMapX + 8;
                    int[] gridPoint = MapView.SetGridPosition(contact.charPosOnMapX, contact.charPosOnMapY + 52);
                    charOngridX = gridPoint[0];
                    charOngridY = gridPoint[1];
                    if ((contact.charPosOnMapX / 8) % 8 == 2) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-0.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 3) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-1.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 4) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-2.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 10, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 5) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-3.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 1, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 6) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-4.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 7) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-5.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 0) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-6.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 10, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 1) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-1-7.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    }
                } else if (ClickPositionForCharX < contact.charPosOnMapX) {
                    contact.direction = 4;
                    contact.charPosOnMapX = contact.charPosOnMapX - 8;
                    int[] gridPoint = MapView.SetGridPosition(contact.charPosOnMapX, contact.charPosOnMapY + 52);
                    charOngridX = gridPoint[0];
                    charOngridY = gridPoint[1];
                    if ((contact.charPosOnMapX / 8) % 8 == 2) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-7.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 3) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-6.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 10, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 4) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-5.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 5) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-4.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 3, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 6) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-3.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 2, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 7) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-2.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX - 10, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 0) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-1.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX, contact.charPosOnMapY);
                    } else if ((contact.charPosOnMapX / 8) % 8 == 1) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAB/HMMAXXAB.gif-4-0.gif");
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 3, contact.charPosOnMapY);
                    }
                } else {
                    if (contact.direction == 0) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAA/HMMAXXAA.gif-0-00.gif");
                    } else if (contact.direction == 1) {
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 10, contact.charPosOnMapY);
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAA/HMMAXXAA.gif-1-00.gif");
                    } else if (contact.direction == 2) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAA/HMMAXXAA.gif-2-00.gif");
                    } else if (contact.direction == 3) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAA/HMMAXXAA.gif-3-00.gif");
                    } else if (contact.direction == 4) {
                        map.setWidgetPosition(characterImage, contact.charPosOnMapX + 10, contact.charPosOnMapY);
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAA/HMMAXXAA.gif-4-00.gif");
                    } else if (contact.direction == 5) {
                        characterImage.setUrl("./gfx/critter/art/critters/HMMAXXAA/HMMAXXAA.gif-5-00.gif");
                    }
                }
            }
        };
        charTimer.scheduleRepeating(100);
    }

    Timer hitTimer;

    public void characterHitAnimation() {
        if (situation.equals("NOOP")) {
            situation = "HIT";
            charTimer.cancel();
            hitTimer = new Timer() {

                int i = 0;

                public void run() {
                    characterImage.setUrl("./gfx/critter/art/" + "critters/HMMAXXAQ/HMMAXXAQ-" + contact.direction + "-0" + i + ".gif");
                    i++;
                    if (i == 10) {
                        hitTimer.cancel();
                        charTimer.scheduleRepeating(100);
                        situation = "NOOP";
                    }
                    if (i > 10) {
                        hitTimer.cancel();
                        charTimer.scheduleRepeating(100);
                        situation = "NOOP";
                    }
                }
            };
            hitTimer.scheduleRepeating(100);
        }
    }

    Timer damageTimer;

    public void getDamageAnimation() {
        if (situation.equals("NOOP")) {
            situation = "DMG";
            charTimer.cancel();
            damageTimer = new Timer() {

                int i = 0;

                public void run() {
                    characterImage.setUrl("./gfx/critter/art/" + "critters/HMMAXXAO/HMMAXXAO-" + contact.direction + "-" + i + ".gif");
                    i++;
                    if (i == 6) {
                        damageTimer.cancel();
                        charTimer.scheduleRepeating(100);
                        situation = "NOOP";
                    }
                    if (i > 10) {
                        damageTimer.cancel();
                        charTimer.scheduleRepeating(100);
                        situation = "NOOP";
                    }
                }
            };
            damageTimer.scheduleRepeating(100);
        }
    }
}
