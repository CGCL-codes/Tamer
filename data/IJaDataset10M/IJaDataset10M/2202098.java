package org.idontknow.wordpath;

class WordPath {

    public static void main(String[] args) {
        WordPath instance = new WordPath();
        int count;
        int nbRows = Integer.valueOf(args[0]).intValue();
        String[] grid = new String[nbRows];
        for (int i = 0; i < nbRows; i++) grid[i] = args[i + 1];
        String find = args[nbRows + 1];
        count = instance.countPaths(grid, find);
        System.out.println("I found " + count + " " + (count > 1 ? "paths" : "path") + " for this string:)\n");
    }

    public int countPaths(String[] grid, String find) {
        int nbRow = grid.length;
        int nbCol = grid[0].length();
        int i, j;
        int count = 0;
        for (i = 0; i < nbRow; i++) for (j = 0; j < nbCol; j++) if (grid[i].charAt(j) == find.charAt(0)) {
            int temp = findFromPosition(grid, i, j, find.substring(1));
            if (temp == -1) return -1; else count += temp;
        }
        return count;
    }

    public int findFromPosition(String[] grid, int posRow, int posCol, String find) {
        int count = 0;
        int temp;
        for (int i = posRow - 1; i <= posRow + 1; i++) for (int j = posCol - 1; j <= posCol + 1; j++) {
            if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length() || (i == posRow && j == posCol)) continue; else if (grid[i].charAt(j) == find.charAt(0) && find.length() > 1) {
                temp = findFromPosition(grid, i, j, find.substring(1));
                if (temp == -1) return -1; else count += temp;
            } else if (grid[i].charAt(j) == find.charAt(0)) count++;
        }
        return (count > 100000000 ? -1 : count);
    }
}
