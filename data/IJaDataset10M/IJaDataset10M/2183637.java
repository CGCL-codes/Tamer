package model;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

enum Command {

    Square, Circle, Triangle, RightFlipper, LeftFlipper, Absorber, Ball, Rotate, Delete, Move, Connect, KeyConnect, Gravity, Friction
}

public class GizmoParser {

    public static void readFile(int L, File fileName, Board board) throws IOException {
        File file = new File(fileName.getName());
        ArrayList<String[]> objects = new ArrayList<String[]>();
        try {
            Scanner scanner = new Scanner(fileName);
            while (scanner.hasNextLine()) {
                String strLine = scanner.nextLine();
                if (strLine.trim().length() == 0) continue;
                String[] args = strLine.split(" ");
                objects.add(args);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String[] arg : objects) {
            Command command = null;
            IGizmo gizmo = null;
            int x, y;
            for (Command cmd : Command.values()) {
                if (cmd.toString().equalsIgnoreCase(arg[0])) command = cmd;
            }
            switch(command) {
                case Square:
                    x = Integer.parseInt(arg[2].trim());
                    y = Integer.parseInt(arg[3].trim());
                    gizmo = new SquareBumper(L, arg[1]);
                    board.addGizmo(gizmo, x, y);
                    break;
                case Circle:
                    x = Integer.parseInt(arg[2].trim());
                    y = Integer.parseInt(arg[3].trim());
                    gizmo = new CircleBumper(L, arg[1]);
                    board.addGizmo(gizmo, x, y);
                    break;
                case Triangle:
                    x = Integer.parseInt(arg[2].trim());
                    y = Integer.parseInt(arg[3].trim());
                    gizmo = new TriangleBumper(L, arg[1]);
                    board.addGizmo(gizmo, x, y);
                    break;
                case RightFlipper:
                    x = Integer.parseInt(arg[2].trim());
                    y = Integer.parseInt(arg[3].trim());
                    gizmo = new RightFlipper(L, arg[1]);
                    board.addGizmo(gizmo, x, y);
                    break;
                case LeftFlipper:
                    x = Integer.parseInt(arg[2].trim());
                    y = Integer.parseInt(arg[3].trim());
                    gizmo = new LeftFlipper(L, arg[1]);
                    board.addGizmo(gizmo, x, y);
                    break;
                case Absorber:
                    break;
                case Ball:
                    break;
                case Rotate:
                    for (int i = 0; i < board.getGizmos().size(); i++) {
                        if (board.getGizmos().get(i).getName().equalsIgnoreCase(arg[1])) board.getGizmos().get(i).rotateClockwise();
                    }
                    break;
                case Delete:
                    for (int i = 0; i < board.getGizmos().size(); i++) {
                        if (board.getGizmos().get(i).getName().equalsIgnoreCase(arg[1])) board.removeGizmo(board.getGizmos().get(i).getX(), board.getGizmos().get(i).getY());
                    }
                    break;
                case Move:
                    break;
                case Connect:
                    break;
                case KeyConnect:
                    break;
                case Gravity:
                    break;
                case Friction:
                    break;
            }
        }
        for (int i = 0; i <= 19; i++) {
            board.addGizmo(new Absorber(board.getL(), "new"), i, 19);
        }
    }
}
