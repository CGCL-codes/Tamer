package demos.util;

import java.io.*;
import java.nio.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import com.sun.opengl.util.texture.spi.*;

/** Simplified clone of DxTex tool from the DirectX SDK, written in
    Java using the DDSImage; tests fetching of texture data */
public class DxTex {

    private InternalFrameListener frameListener;

    private File defaultDirectory;

    private JDesktopPane desktop;

    private static String endl = System.getProperty("line.separator");

    private JMenu mipMapMenu;

    public static void main(String[] args) {
        new DxTex().run(args);
    }

    private void run(String[] args) {
        defaultDirectory = new File(System.getProperty("user.dir"));
        JFrame frame = new JFrame("DirectX Texture Tool");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = createMenu("File", 'F', 0);
        JMenuItem item = createMenuItem("Open...", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        }, KeyEvent.VK_O, InputEvent.CTRL_MASK, 'O', 0);
        menu.add(item);
        item = createMenuItem("Exit", new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }, KeyEvent.VK_Q, InputEvent.CTRL_MASK, 'x', 1);
        menu.add(item);
        menuBar.add(menu);
        menu = createMenu("MipMap", 'M', 0);
        menu.setEnabled(false);
        mipMapMenu = menu;
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        desktop = new JDesktopPane();
        frame.getContentPane().add(desktop);
        frame.setSize(640, 480);
        frame.show();
        frameListener = new InternalFrameAdapter() {

            public void internalFrameActivated(InternalFrameEvent e) {
                JInternalFrame ifr = e.getInternalFrame();
                if (ifr instanceof ImageFrame) {
                    final ImageFrame frame = (ImageFrame) ifr;
                    if (frame.getNumMipMaps() > 0) {
                        mipMapMenu.removeAll();
                        for (int i = 0; i < frame.getNumMipMaps(); i++) {
                            final int map = i;
                            JMenuItem item;
                            String title = "Level " + (i + 1);
                            ActionListener listener = new ActionListener() {

                                public void actionPerformed(ActionEvent e) {
                                    SwingUtilities.invokeLater(new Runnable() {

                                        public void run() {
                                            frame.setMipMapLevel(map);
                                        }
                                    });
                                }
                            };
                            if (i < 9) {
                                char c = (char) ('0' + i + 1);
                                item = createMenuItem(title, listener, c, 6);
                            } else {
                                item = createMenuItem(title, listener);
                            }
                            mipMapMenu.add(item);
                        }
                        mipMapMenu.setEnabled(true);
                    } else {
                        mipMapMenu.setEnabled(false);
                    }
                } else {
                    mipMapMenu.setEnabled(false);
                }
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                desktop.remove(e.getInternalFrame());
                desktop.invalidate();
                desktop.validate();
                desktop.repaint();
                desktop.requestFocus();
            }

            public void internalFrameClosed(InternalFrameEvent e) {
                JInternalFrame ifr = e.getInternalFrame();
                if (ifr instanceof ImageFrame) {
                    ((ImageFrame) ifr).close();
                }
            }
        };
        for (int i = 0; i < args.length; i++) {
            final File file = new File(args[i]);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    openFile(file);
                }
            });
        }
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser(defaultDirectory);
        chooser.setMultiSelectionEnabled(false);
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                return (f.isDirectory() || f.getName().endsWith(".dds"));
            }

            public String getDescription() {
                return "Texture files (*.dds)";
            }
        });
        int res = chooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            defaultDirectory = file.getParentFile();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    openFile(file);
                }
            });
        }
    }

    private void openFile(File file) {
        try {
            DDSImage image = DDSImage.read(file);
            showImage(file.getName(), image, 0);
        } catch (IOException e) {
            showMessageDialog("Error while opening file:" + endl + exceptionToString(e), "Error opening file", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showImage(String filename, DDSImage image, int mipMapLevel) {
        try {
            ImageFrame fr = new ImageFrame(filename, image, mipMapLevel);
            desktop.add(fr);
            fr.show();
        } catch (Exception e) {
            showMessageDialog("Error while loading file:" + endl + exceptionToString(e), "Error loading file", JOptionPane.WARNING_MESSAGE);
        }
    }

    class ImageFrame extends JInternalFrame {

        private String filename;

        private DDSImage image;

        private int mipMapLevel;

        private int curWidth;

        private int curHeight;

        private JLabel label;

        ImageFrame(String filename, DDSImage image, int mipMapLevel) {
            super();
            this.filename = filename;
            this.image = image;
            addInternalFrameListener(frameListener);
            label = new JLabel();
            JScrollPane scroller = new JScrollPane(label);
            getContentPane().add(scroller);
            setSize(400, 400);
            setResizable(true);
            setIconifiable(true);
            setClosable(true);
            setMipMapLevel(mipMapLevel);
        }

        int getNumMipMaps() {
            return image.getNumMipMaps();
        }

        void setMipMapLevel(int level) {
            mipMapLevel = level;
            computeImage();
            resetTitle();
        }

        void close() {
            System.err.println("Closing files");
            image.close();
        }

        private void computeImage() {
            image.getNumMipMaps();
            DDSImage.ImageInfo info = image.getMipMap(mipMapLevel);
            int width = info.getWidth();
            int height = info.getHeight();
            curWidth = width;
            curHeight = height;
            ByteBuffer data = info.getData();
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            WritableRaster dst = img.getRaster();
            int skipSize;
            if (image.getPixelFormat() == DDSImage.D3DFMT_A8R8G8B8) {
                skipSize = 4;
            } else if (image.getPixelFormat() == DDSImage.D3DFMT_R8G8B8) {
                skipSize = 3;
            } else {
                image.close();
                throw new RuntimeException("Unsupported pixel format " + image.getPixelFormat());
            }
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    dst.setSample(x, y, 0, data.get(skipSize * (width * y + x) + 2) & 0xFF);
                    dst.setSample(x, y, 1, data.get(skipSize * (width * y + x) + 1) & 0xFF);
                    dst.setSample(x, y, 2, data.get(skipSize * (width * y + x) + 0) & 0xFF);
                }
            }
            label.setIcon(new ImageIcon(img));
        }

        private void resetTitle() {
            setTitle(filename + " (" + curWidth + "x" + curHeight + ", mipmap " + (1 + mipMapLevel) + " of " + image.getNumMipMaps() + ")");
        }
    }

    private static JMenu createMenu(String name, char mnemonic, int mnemonicPos) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);
        menu.setDisplayedMnemonicIndex(mnemonicPos);
        return menu;
    }

    private static JMenuItem createMenuItem(String name, ActionListener l) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(l);
        return item;
    }

    private static JMenuItem createMenuItemInternal(String name, ActionListener l, int accelerator, int modifiers) {
        JMenuItem item = createMenuItem(name, l);
        item.setAccelerator(KeyStroke.getKeyStroke(accelerator, modifiers));
        return item;
    }

    private static JMenuItem createMenuItem(String name, ActionListener l, int accelerator) {
        return createMenuItemInternal(name, l, accelerator, 0);
    }

    private static JMenuItem createMenuItem(String name, ActionListener l, char mnemonic, int mnemonicPos) {
        JMenuItem item = createMenuItem(name, l);
        item.setMnemonic(mnemonic);
        item.setDisplayedMnemonicIndex(mnemonicPos);
        return item;
    }

    private static JMenuItem createMenuItem(String name, ActionListener l, int accelerator, int acceleratorMods, char mnemonic, int mnemonicPos) {
        JMenuItem item = createMenuItemInternal(name, l, accelerator, acceleratorMods);
        item.setMnemonic(mnemonic);
        item.setDisplayedMnemonicIndex(mnemonicPos);
        return item;
    }

    private void showMessageDialog(final String message, final String title, final int jOptionPaneKind) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JOptionPane.showInternalMessageDialog(desktop, message, title, jOptionPaneKind);
            }
        });
    }

    private static String exceptionToString(Exception e) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(bos);
        e.printStackTrace(s);
        return bos.toString();
    }
}
