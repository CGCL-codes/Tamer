package net.ar.guia.own.interfaces;

public interface ButtonGroupComponent extends PanelComponent {

    public abstract ButtonComponent getSelectedButton();

    public abstract void setSelectedButton(ButtonComponent selectedButton);
}
