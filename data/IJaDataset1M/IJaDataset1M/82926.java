package jdc.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class Encoder {

    private class Comparator implements java.util.Comparator {

        public int compare(java.lang.Object a, java.lang.Object b) {
            Node aa = (Node) a;
            Node bb = (Node) b;
            if (aa.weight > bb.weight) return 1; else if (aa.weight == bb.weight) return 0; else return -1;
        }
    }

    private Vector _tuples = new Vector();

    private Node _root_node = null;

    private Integer _number_of_tuples = null;

    private Node[] _codearray = new Node[256];

    private Integer _checksum = null;

    private BitStream _bitstream = new BitStream();

    public String encode(byte[] input) {
        String encoded_buf = new String("");
        _buildFreqTableAndChksum(input);
        _buildTree();
        _buildBitStrings(_root_node, new String());
        encoded_buf += _addHeader(input);
        encoded_buf += _addTuples();
        _addTreeToBitStream();
        _bitstream.nextWholeByte();
        _compressInput(input);
        _bitstream.nextWholeByte();
        encoded_buf += _bitstream.bitStream();
        return encoded_buf;
    }

    private void _buildTree() {
        Collections.sort(_tuples, new Comparator());
        Iterator itr = _tuples.iterator();
        while (_tuples.size() > 1) {
            Node left = (Node) _tuples.remove(0);
            Node right = (Node) _tuples.remove(0);
            Node to_add = new Node(left, right);
            if (_tuples.size() == 0 || to_add.weight > ((Node) _tuples.lastElement()).weight) {
                _tuples.add(to_add);
            } else {
                for (int i = 0; i < _tuples.size(); i++) {
                    if (to_add.weight <= ((Node) _tuples.get(i)).weight) {
                        _tuples.add(i, to_add);
                        break;
                    }
                }
            }
            itr = _tuples.iterator();
        }
        _root_node = (Node) _tuples.get(0);
        _tuples.remove(0);
    }

    private void _buildBitStrings(Node node, String bits) {
        if (node.leaf) {
            node.bitstring = bits;
            node.number_of_bits = bits.length();
            _codearray[node.character] = node;
        } else {
            _buildBitStrings(node.left, bits + "0");
            _buildBitStrings(node.right, bits + "1");
        }
    }

    private void _buildFreqTableAndChksum(byte[] buffer) {
        int character;
        int chksum = 0;
        for (int i = 0; i < 256; i++) {
            _tuples.add(i, new Node((char) i));
        }
        for (int i = 0; i < buffer.length; i++) {
            character = (int) buffer[i] & 0xff;
            ((Node) _tuples.elementAt(character)).weight++;
            chksum ^= character;
        }
        Iterator itr = _tuples.iterator();
        while (itr.hasNext()) {
            if (((Node) itr.next()).weight == 0) {
                itr.remove();
            }
        }
        _checksum = new Integer(chksum);
        _number_of_tuples = new Integer(_tuples.size());
    }

    private String _addHeader(byte[] input) {
        String ret_string = new String();
        ret_string += "HE3\r";
        ret_string += (char) ((_checksum.intValue()) & 0xff);
        ret_string += (char) (input.length & 0xff);
        ret_string += (char) ((input.length >> 8) & 0xff);
        ret_string += (char) ((input.length >> 16) & 0xff);
        ret_string += (char) ((input.length >> 24) & 0xff);
        ret_string += (char) (_number_of_tuples.intValue() & 0xff);
        ret_string += (char) ((_number_of_tuples.intValue() >> 8) & 0xff);
        return ret_string;
    }

    private void _compressInput(byte[] input) {
        for (int i = 0; i < input.length; i++) {
            Node bitstring_node = (Node) _codearray[input[i] & 0xff];
            _bitstream.addLSBFirst(bitstring_node.bitstring);
        }
    }

    private String _addTuples() {
        String ret_string = new String("");
        Node node = null;
        for (int i = 0; i < 256; i++) {
            node = _codearray[i];
            if (node != null) {
                ret_string += (char) (node.character & 0xff);
                ret_string += (char) (node.number_of_bits & 0xff);
            }
        }
        return ret_string;
    }

    private void _addTreeToBitStream() {
        Node node = null;
        for (int i = 0; i < 256; i++) {
            node = _codearray[i];
            if (node != null) {
                _bitstream.addLSBFirst(node.bitstring);
            }
        }
    }
}
