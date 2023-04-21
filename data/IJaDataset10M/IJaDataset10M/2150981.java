package com.cevatify.logic.handler;

import android.content.Context;
import android.view.Gravity;
import android.widget.*;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Notifier {

    private Toast toastNotifier;

    private Context gameContext;

    private AlertDialog.Builder finishedBox, givenUpBox;

    private DialogInterface.OnClickListener onFinishOkClick;

    private DialogInterface.OnClickListener onFinishCancelClick;

    private DialogInterface.OnClickListener onGiveUpOkClick;

    private DialogInterface.OnClickListener onGiveUpCancelClick;

    public Notifier(Context c) {
        initNotifier(c);
    }

    private void initNotifier(Context c) {
        gameContext = c;
        initFinishBox();
        initGiveUpBox();
        setToastNotifyMessage("toast notification!");
    }

    private void initFinishBox() {
        finishedBox = new AlertDialog.Builder(gameContext);
    }

    private void initGiveUpBox() {
        givenUpBox = new AlertDialog.Builder(gameContext);
    }

    public void showFinishBox() {
        finishedBox.setTitle("Time up!");
        finishedBox.setMessage("Sorry dude, Your time is up :/ Do you want to play again?");
        finishedBox.setPositiveButton("Yes", onFinishOkClick);
        finishedBox.setNegativeButton("No", onFinishCancelClick);
        finishedBox.show();
    }

    public void showGiveUpBox() {
        givenUpBox.setTitle("Given Up!");
        givenUpBox.setMessage("You Have Failed!!  Do you want to play again?");
        givenUpBox.setPositiveButton("Yes", onGiveUpOkClick);
        givenUpBox.setNegativeButton("No", onGiveUpCancelClick);
        givenUpBox.show();
    }

    public void setOnFinishOkClick(DialogInterface.OnClickListener oCL) {
        this.onFinishOkClick = oCL;
    }

    public void setOnFinishCancelClick(DialogInterface.OnClickListener oCL) {
        this.onFinishCancelClick = oCL;
    }

    public void setOnGiveUpOkClick(DialogInterface.OnClickListener oCL) {
        this.onGiveUpOkClick = oCL;
    }

    public void setOnGiveUpCancelClick(DialogInterface.OnClickListener oCL) {
        this.onGiveUpCancelClick = oCL;
    }

    public void setToastNotifyMessage(String msg) {
        toastNotifier = Toast.makeText(gameContext, msg, Toast.LENGTH_LONG);
        toastNotifier.setGravity(Gravity.TOP, toastNotifier.getXOffset() / 2, toastNotifier.getYOffset() / 2);
    }

    public void printToastNotifyMessage(String msg) {
        setToastNotifyMessage(msg);
        toastNotifier.show();
    }
}
