package com.mascotikas.client.users;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

/**
 *
 * @author Unisysad
 */
public class MainUser extends Canvas {

    public MainUser() {
        HStack hStack = new HStack(10);
        hStack.setLayoutMargin(10);
        hStack.setLeft(120);
        hStack.setShowEdges(true);
        hStack.setEdgeImage("edges/blue/6.png");
        hStack.setCanAcceptDrop(true);
        hStack.setAnimateMembers(true);
        hStack.setShowDragPlaceHolder(true);
        hStack.setBorder("1px solid #4040ff");
        hStack.addMember(new DragPiece("cube_blue.png"));
        hStack.addMember(new DragPiece("cube_green.png"));
        hStack.addMember(new DragPiece("cube_yellow.png"));
        VStack vStack = new VStack(10);
        vStack.setLayoutMargin(10);
        vStack.setShowEdges(true);
        vStack.setEdgeImage("edges/green/6.png");
        vStack.setCanAcceptDrop(true);
        vStack.setAnimateMembers(true);
        vStack.setDropLineThickness(4);
        Canvas dropLineProp = new Canvas();
        dropLineProp.setBackgroundColor("#40c040");
        vStack.setDropLineProperties(dropLineProp);
        vStack.addMember(new DragPiece("cube_blue.png"));
        vStack.addMember(new DragPiece("cube_green.png"));
        vStack.addMember(new DragPiece("cube_yellow.png"));
        addChild(hStack);
        addChild(vStack);
    }

    private class DragPiece extends Img {

        public DragPiece() {
            setWidth(48);
            setHeight(48);
            setLayoutAlign(Alignment.CENTER);
            setCanDragReposition(true);
            setCanDrop(true);
            setDragAppearance(DragAppearance.TARGET);
            setAppImgDir("pieces/48/");
        }

        public DragPiece(String src) {
            this();
            setSrc(src);
        }
    }
}
