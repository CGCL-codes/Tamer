package org.silicolife.bonzai.maple.android.view;

import java.util.ArrayList;
import org.silicolife.bonzai.maple.model.Maple;
import org.silicolife.bonzai.maple.model.MapleStem;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;

public class MapleTree {

    private float initial_start_x = 0.5f;

    private float initial_start_y = 1f;

    private int initial_max_angle = 20;

    private float initial_length = 0.2f;

    private float initial_thickness = 0.08f;

    public static class Branch {

        private float startX;

        private float startY;

        private float endX;

        private float endY;

        private float thickness;

        private ShapeDrawable branchShape;

        public Branch(float startX, float startY, float endX, float endY, float thickness, ShapeDrawable branchShape) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.thickness = thickness;
            this.branchShape = branchShape;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }

        public float getEndX() {
            return endX;
        }

        public float getEndY() {
            return endY;
        }

        public float getThickness() {
            return thickness;
        }

        public ShapeDrawable getBranchShape() {
            return branchShape;
        }
    }

    ArrayList<Branch> branches = null;

    MapleBranchFactory mapleBranchFactory = null;

    int lastTreeAge = 0;

    public MapleTree(int viewWidth, int viewHeight) {
        branches = new ArrayList<Branch>();
        mapleBranchFactory = new MapleBranchFactory();
        mapleBranchFactory.setBounds(new Rect(0, 0, viewWidth, viewHeight));
    }

    public void drawTree(Canvas canvas, int viewWidth, int viewHeight, Maple tree) {
        mapleBranchFactory.setBounds(new Rect(0, 0, viewWidth, viewHeight));
        ageTree(tree);
        growBuds(tree, branches);
        drawBranches(canvas, branches);
    }

    public void ageTree(Maple tree) {
        for (int age = lastTreeAge; age < tree.getAge(); age++) {
            initial_length -= (Math.random() * initial_length / 10);
        }
        lastTreeAge = tree.getAge();
    }

    public void growBuds(Maple tree, ArrayList<Branch> branchs) {
        MapleStem stem = (MapleStem) tree.getLivingCell("MapleStem");
        if (stem != null) {
            int terminalBranchCount = stem.getTerminalBranchCount();
            for (int i = branches.size(); i < terminalBranchCount; i++) {
                growBud(branches);
            }
        }
    }

    public void growBud(ArrayList<Branch> branchs) {
        float startX = initial_start_x;
        float startY = initial_start_y;
        if (branchs.size() > 0) {
            Branch lastBranch = branchs.get(branchs.size() - 1);
            startX = lastBranch.getEndX();
            startY = lastBranch.getEndY();
        }
        int angle = (int) (Math.random() * initial_max_angle * 2) - initial_max_angle;
        float length = initial_length;
        float thickness = initial_thickness;
        float endX = startX + (float) (length * Math.sin(Math.toRadians(angle)));
        float endY = startY - (float) (length * Math.cos(Math.toRadians(angle)));
        ShapeDrawable branchShape = mapleBranchFactory.createMapleBranch(startX, startY, endX, endY, thickness);
        branchs.add(new Branch(startX, startY, endX, endY, thickness, branchShape));
    }

    public void drawBranches(Canvas canvas, ArrayList<Branch> branchs) {
        for (Branch branch : branches) {
            drawBranch(canvas, branch);
        }
    }

    public void drawBranch(Canvas canvas, Branch branch) {
        branch.getBranchShape().draw(canvas);
    }
}
