package org.jspar.matrix;

import org.jspar.model.Module;

public class ConnectionMatrix {

    private int size;

    private Module[] modules;

    private int[][] values;

    public ConnectionMatrix(Module[] modules) {
        size = modules.length;
        values = new int[size][size];
        this.modules = new Module[modules.length];
        for (int i = 0; i < size; ++i) {
            this.modules[i] = modules[i];
            for (int j = 0; j < size; ++j) {
                values[i][j] = 0;
            }
        }
    }

    public ConnectionMatrix(ConnectionMatrix matrix) {
        this.size = matrix.size;
        this.values = new int[size][size];
        this.modules = new Module[matrix.modules.length];
        for (int i = 0; i < size; ++i) {
            this.modules[i] = matrix.modules[i];
            for (int j = 0; j < size; ++j) {
                this.values[i][j] = matrix.values[i][j];
            }
        }
    }

    public int size() {
        return size;
    }

    public int valueAt(int i, int j) {
        return values[i][j];
    }

    public int valueAt(Module m) {
        int i = findModuleIndex(m);
        return values[i][i];
    }

    public int valueAt(Module mi, Module mj) {
        int i = findModuleIndex(mi);
        int j = findModuleIndex(mj);
        return values[i][j];
    }

    public void incrementAt(Module mi, Module mj) {
        int i = findModuleIndex(mi);
        int j = findModuleIndex(mj);
        ++values[i][j];
    }

    private int findModuleIndex(Module m) {
        for (int i = 0; i < modules.length; ++i) {
            if (modules[i] == m) return i;
        }
        throw new RuntimeException("cant find module " + m.name() + " in matrix");
    }

    public double connectionRatio(int i, int j) {
        return ((double) (values[i][i] + values[j][j] - values[j][i] - values[i][j])) / (values[i][i] + values[j][j]);
    }

    public int externalConnections(int i, int j) {
        return values[i][i] + values[j][j] - values[j][i] - values[i][j];
    }

    public void merge(int a, int b) {
        values[a][a] += values[b][b] - values[a][b] - values[b][a];
        for (int i = 0; i < size; ++i) {
            if (i != a) values[i][a] += values[i][b];
        }
        for (int i = 0; i < size; ++i) {
            if (i != a) values[a][i] += values[b][i];
        }
        remove(b);
    }

    public void remove(int r) {
        for (int i = r; i < (size - 1); ++i) {
            modules[i] = modules[i + 1];
            for (int j = 0; j < size; ++j) {
                values[i][j] = values[i + 1][j];
            }
        }
        for (int i = 0; i < (size - 1); ++i) {
            for (int j = r; j < (size - 1); ++j) {
                values[i][j] = values[i][j + 1];
            }
        }
        size--;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('|');
        for (int i = 0; i < size; ++i) {
            if (i != 0) buffer.append(' ');
            buffer.append(modules[i].name());
        }
        buffer.append("|\n");
        for (int i = 0; i < size; ++i) {
            buffer.append(modules[i].name());
            buffer.append("\t|");
            for (int j = 0; j < size; ++j) {
                if (j != 0) buffer.append(' ');
                buffer.append(values[i][j]);
            }
            buffer.append("|\n");
        }
        return buffer.toString();
    }
}
