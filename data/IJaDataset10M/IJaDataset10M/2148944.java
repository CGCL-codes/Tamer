package edu.ksu.cis.bnj.ver3.inference.exact.mtls;

public class MessageQueue {

    private QueueElement _Head;

    private QueueElement _Tail;

    class QueueElement {

        public Message _M;

        public QueueElement _Next;
    }

    public synchronized void add(Message M) {
        QueueElement QE = new QueueElement();
        QE._M = M;
        QE._Next = null;
        if (_Tail == null) {
            _Head = QE;
            _Tail = QE;
        } else {
            _Tail._Next = QE;
            _Tail = QE;
        }
    }

    public MessageQueue() {
        _Head = null;
        _Tail = null;
    }

    public synchronized Message get(MClique[] CheckOut, int Proc) {
        QueueElement _Search = _Head;
        QueueElement _SearchLast = null;
        while (_Search != null) {
            Message M = _Search._M;
            if (M.getClique().getProcessor() < 0) {
                CheckOut[Proc] = M.getClique();
                CheckOut[Proc].assignProcessor(Proc);
                if (_SearchLast == null) {
                    _Head = _Search._Next;
                    if (_Head == null) {
                        _Tail = null;
                    }
                } else {
                    _SearchLast._Next = _Search._Next;
                    if (_SearchLast._Next == null) {
                        _Tail = _SearchLast;
                    }
                }
                return M;
            }
            _SearchLast = _Search;
            _Search = _Search._Next;
        }
        return null;
    }
}
